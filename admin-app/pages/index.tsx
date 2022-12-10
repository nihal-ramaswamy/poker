import { CompatClient, Stomp } from "@stomp/stompjs";
import type { NextPage } from "next";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import axios from "../config/axios";
import { GAME_SERVICE_WS_URL } from "../config/constants";

let stompClient: CompatClient | null = null;

const Home: NextPage = () => {
  const [username, setUsername] = useState<string>("");
  const [gameID, setGameID] = useState<string>("");
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);

  const createGame = async () => {
    const {
      data: { gameID },
    } = await axios.post("/game/create", {
      adminUsername: "admin-username",
      settings: {
        startingMoney: 10,
        smallBet: 10,
        bigBet: 10,
        startingBetSettings: {
          growthAmount: 10,
          perRoundCount: 10,
        },
        timeLimit: 10,
        numberOfDecks: 10,
        exposeWinnerDetails: false,
      },
    });
    localStorage.setItem("gameID", gameID);
    localStorage.setItem("username", username);
    setGameID(gameID);
    setIsLoggedIn(true);
  };

  const connect = () => {
    const socket = new SockJS(GAME_SERVICE_WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;
    stompClient.connect({}, async function () {
      console.log("Connected");
    });
  };

  useEffect(() => {
    if (stompClient == null) {
      connect();
    }
  }, []);

  useEffect(() => {
    if (!isLoggedIn) {
      const gameID = localStorage.getItem("gameID"),
        username = localStorage.getItem("username");
      if (gameID && username) {
        setGameID(gameID);
        setUsername(username);
        setIsLoggedIn(true);
      }
    }
  }, []);

  return (
    <div>
      {isLoggedIn ? (
        <p>
          Logged In: {username}, gameID: {gameID}
        </p>
      ) : (
        <div className="p-5">
          <div className="mb-5">
            <label className="mr-3">Username:</label>
            <input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="shadow appearance-none border rounded w-56 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              type="text"
            />
          </div>

          <button
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            type="button"
            onClick={createGame}
          >
            Create game
          </button>
        </div>
      )}
    </div>
  );
};

export default Home;
