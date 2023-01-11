import { CompatClient, Stomp } from "@stomp/stompjs";
import type { NextPage } from "next";
import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import axios from "../config/axios";
import {
  GAME_SERVICE_BASE_URL,
  GAME_SERVICE_WS_URL,
} from "../config/constants";
import Player from "../types/Player";

let stompClient: CompatClient | null = null;

const Home: NextPage = () => {
  const [username, setUsername] = useState<string>("");
  const [gameID, setGameID] = useState<string>("");
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [players, setPlayers] = useState<Player[]>([]);
  const [hasStarted, setHasStarted] = useState<boolean>(false);

  const createGame = async () => {
    const {
      data: { gameID },
    } = await axios.post("/game/create", {
      adminUsername: username,
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

  const fetchCurrentGameStatus = async () => {
    const { data } = await axios.get(`/game/${gameID}`);
    const { players, hasStarted } = data;
    setPlayers(players);
    setHasStarted(hasStarted);
  };

  const connect = () => {
    const socket = new SockJS(GAME_SERVICE_WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;
    stompClient.connect({}, async () => {
      console.log("Connected.");
      fetchCurrentGameStatus();

      const onJoinURL = `/admin/${gameID}/on-join`;
      stompClient?.subscribe(onJoinURL, (message) => {
        const { playerUsername, playerID } = JSON.parse(message.body);
        setPlayers((players) => [
          ...players,
          { id: playerID, username: playerUsername },
        ]);
      });
    });
  };

  useEffect(() => {
    if (isLoggedIn && stompClient == null) {
      connect();
    }
  }, [isLoggedIn]);

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

  const startGame = async () => {
    try {
      const res = await axios.get(
        `${GAME_SERVICE_BASE_URL}/game/${gameID}/start`
      );
      console.log(res.data);
    } catch (err: any) {
      console.log(err.response);
    }
  };

  return (
    <div>
      {isLoggedIn ? (
        <div>
          <p className="mb-3">
            Logged In: {username}, gameID: {gameID}
          </p>
          <div>
            <label className="text-md font-medium text-gray-900">
              Players: {players.length}
            </label>
            <ul>
              {players.map(({ username, id }) => (
                <li key={`player-${id}`}>{username}</li>
              ))}
            </ul>
          </div>
          <button
            className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
            onClick={startGame}
            type="button"
          >
            Start game
          </button>
        </div>
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
