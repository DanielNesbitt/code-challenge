import {applyMiddleware, combineReducers, compose, createStore} from 'redux'
import {TypedUseSelectorHook, useSelector} from "react-redux";
import {LoginModule, LoginState} from "../routes/Login/LoginModule";
import {HomeModule, HomeState} from "../routes/Home/HomeModule";

export type ApplicationState = LoginState & HomeState;

// -------------------- Store Enhancers --------------------

const __DEV__ = true;       // todo
const enhancers = [applyMiddleware()];
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
}), {}, compose(...enhancers),);

// -------------------- Selectors --------------------

export const useTypedSelector: TypedUseSelectorHook<ApplicationState> = useSelector;
