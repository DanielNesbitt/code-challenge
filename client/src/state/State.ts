import {LoginState} from "../routes/Login/LoginModule";
import {HomeState} from "../routes/Home/HomeModule";
import {QuestionState} from "../routes/Question/QuestionModule";

export type ApplicationState = LoginState & HomeState & QuestionState;
