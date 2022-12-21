import { useEffect, useState } from "react";
import { Button, StyleSheet, Text, TextInput, View } from "react-native";
import axios from "./config/axios";
import { GAME_SERVICE_WS_URL } from "@env";
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient: CompatClient | null = null;

export default function App() {
  const [gameID, setGameID] = useState<string>("");
  const [username, setUsername] = useState<string>("");
  const [playerID, setPlayerID] = useState<string>("");

  const joinGame = async () => {
    console.log(axios.getUri());
    try {
      const res = await axios.post("/game/join", {
          gameID: gameID,
          playerUsername: username,
      });
      console.log(res.data);
      setPlayerID(res.data.playerID);
    } catch (e) {
      console.log(e);
    }
  };

  const connect = () => {
    const socket = new SockJS(GAME_SERVICE_WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;

    stompClient.connect({}, async () => {
      const gameStartURL = `${GAME_SERVICE_WS_URL}/${playerID}/start-game-state`;
      stompClient?.subscribe(gameStartURL, message => {
        console.log(message);
        console.log(JSON.parse(message.body));
      })
    })
  }


  useEffect(() => {
    if (playerID !== "" && stompClient === null) {
      connect();
    }
  }, [playerID]);

  return (
    <View style={styles.container}>
      <View style={styles.labelContainer}>
        <Text style={styles.label}>Username</Text>
      </View>
      <TextInput
        style={styles.input}
        onChangeText={setUsername}
        value={username}
      />
      <View style={styles.labelContainer}>
        <Text style={styles.label}>Game ID</Text>
      </View>
      <TextInput style={styles.input} onChangeText={setGameID} value={gameID} />
      <Button onPress={joinGame} title="Join Game" />
      <Text>{playerID}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
  labelContainer: {
    width: "75%",
  },
  label: {
    fontSize: 18,
  },
  input: {
    height: 40,
    width: 300,
    margin: 12,
    borderWidth: 1,
    padding: 10,
  },
});
