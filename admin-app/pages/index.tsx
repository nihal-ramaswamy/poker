import type { NextPage } from "next";
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useEffect } from "react";
import { GAME_SERVICE_URL } from "../config/constants";

let stompClient: CompatClient | null = null;

const Home: NextPage = () => {
  const connect = () => {
    let socket = new SockJS(GAME_SERVICE_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = () => null;
    stompClient.connect({}, function () {
      console.log("Connected");
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
