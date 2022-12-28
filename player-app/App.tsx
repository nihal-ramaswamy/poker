import { CompatClient, Stomp } from "@stomp/stompjs";
import BigInt from "big-integer";
import { useEffect, useState } from "react";
import { Button, StyleSheet, Text, TextInput, View } from "react-native";
import SockJS from "sockjs-client";
import { TextDecoder, TextEncoder } from "text-encoding";
import axios, { GAME_SERVICE_WS_URL } from "./config/axios";

polyFillTextEncoder();

let stompClient: CompatClient | null = null;

export default function App() {
  const [gameID, setGameID] = useState<string>("");
  const [username, setUsername] = useState<string>("");
  const [playerID, setPlayerID] = useState<string>("");
  const isLoggedIn = playerID !== "";

  const joinGame = async () => {
    try {
      console.log("Joining game:", gameID);
      const {
        data: { playerID },
      } = await axios.post("/game/join", {
        gameID: gameID,
        playerUsername: username,
      });
      console.log("Player ID is:", playerID);
      setPlayerID(playerID);
    } catch (err) {
      console.log("join game failed: ", err);
    }
  };

  const connect = () => {
    const socket = new SockJS(GAME_SERVICE_WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;

    stompClient.connect({}, async () => {
      const gameStartURL = `/player/${playerID}/start-game-state`;
      stompClient?.subscribe(gameStartURL, (message) => {
        const body = JSON.parse(message.body);
        console.log("Game started:", body);
      });
    });
  };

  useEffect(() => {
    if (isLoggedIn && stompClient === null) {
      connect();
    }
  }, [playerID]);

  return (
    <View style={styles.container}>
      {isLoggedIn ? (
        <Text>Player ID: {playerID}</Text>
      ) : (
        <>
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
          <TextInput
            style={styles.input}
            onChangeText={setGameID}
            value={gameID}
          />
          <Button onPress={joinGame} title="Join Game" />
        </>
      )}
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

// TextEncoder polyfill: https://stackoverflow.com/questions/55868484/referenceerror-cant-find-variable-textencoder
function polyFillTextEncoder() {
  Object.assign(global, {
    TextEncoder,
    TextDecoder,
    BigInt,
  });
}
