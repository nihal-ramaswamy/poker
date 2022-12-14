import axios from "axios";
import { GAME_SERVICE_BASE_URL } from "@env";

export default axios.create({ baseURL: GAME_SERVICE_BASE_URL });

export const GAME_SERVICE_WS_URL = `${GAME_SERVICE_BASE_URL}/ws`;
