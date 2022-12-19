import { useState } from "react";
import { Button, StyleSheet, Text, TextInput, View } from "react-native";
import axios from "./config/axios";

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
      setPlayerID(res.data.playerID);
    } catch (e) {
      console.log(e);
    }
  };

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
