import type { NextPage } from "next";
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useEffect } from "react";
import { GAME_SERVICE_WS_URL } from "../config/constants";
import axios from "../config/axios";

let stompClient: CompatClient | null = null;

const Home: NextPage = () => {
  const createGame = async () => {
    const { data } = await axios.post("/game", {
      adminUsername: "admin-username",
      settings: {
        startingMoney: 10,
        smallBet: 10,
        bigBet: 10,
        startingBet: {
          growthRate: 10,
          perRoundCount: 10,
        },
        timeLimit: 10,
        numberOfDecks: 10,
        exposeWinnerDetails: false,
      },
    });
    console.log("data?", data);
  };

  const connect = () => {
    const socket = new SockJS(GAME_SERVICE_WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;
    stompClient.connect({}, async function () {
      console.log("Connected");
      await createGame();
    });
  };

  useEffect(() => {
    if (stompClient == null) {
      connect();
    }
  }, []);

  return <div></div>;
};

export default Home;
