import axios from "axios";
import { GAME_SERVICE_BASE_URL } from "@env";

console.log(GAME_SERVICE_BASE_URL);

export default axios.create({ baseURL: GAME_SERVICE_BASE_URL });
