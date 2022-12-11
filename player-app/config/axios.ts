import axios from "axios";
import { GAME_SERVICE_BASE_URL } from "./constants";

export default axios.create({ baseURL: GAME_SERVICE_BASE_URL });
