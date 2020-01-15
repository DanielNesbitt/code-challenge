import {LoginState} from "../routes/Login/LoginModule";
import {HomeState} from "../routes/Home/HomeModule";

export type ApplicationState = LoginState & HomeState;
