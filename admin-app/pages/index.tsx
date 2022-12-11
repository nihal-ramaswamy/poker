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
          <div className="flex items-center mb-4">
            <label className="text-md mr-3 font-medium text-gray-900">
              Username
            </label>
            <input
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-64 p-2.5"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              type="text"
            />
          </div>
          <button
            className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
            onClick={createGame}
            type="button"
          >
            Create game
          </button>
        </div>
      )}
    </div>
  );
};

export default Home;
