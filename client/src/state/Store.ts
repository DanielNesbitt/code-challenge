import {applyMiddleware, combineReducers, compose, createStore} from 'redux'
import {TypedUseSelectorHook, useSelector} from "react-redux";
import {LoginModule} from "../routes/Login/LoginModule";
import {HomeModule} from "../routes/Home/HomeModule";
import {ApplicationState} from "./State";
import {socketMiddleware} from "./SocketMiddleware";
import {QuestionModule} from "../routes/Question/QuestionModule";
import {SocketModule} from "./SocketModule";

// -------------------- Store Enhancers --------------------

const __DEV__ = true;       // todo
const enhancers = [applyMiddleware(socketMiddleware)];
if (__DEV__) {
    const {__REDUX_DEVTOOLS_EXTENSION__: devToolsExtension} = window as any;
    if (typeof devToolsExtension === 'function') {
        enhancers.push(devToolsExtension({
            actionsBlacklist: [
                '.*keepAlive',
            ],
        }));
    }
}

// -------------------- Store --------------------

export const store = createStore(combineReducers({
    [LoginModule.name]: LoginModule.reducer,
    [HomeModule.name]: HomeModule.reducer,
    [QuestionModule.name]: QuestionModule.reducer,
    [SocketModule.name]: SocketModule.reducer,
}), {}, compose(...enhancers),);

// -------------------- Selectors --------------------

export const useTypedSelector: TypedUseSelectorHook<ApplicationState> = useSelector;
