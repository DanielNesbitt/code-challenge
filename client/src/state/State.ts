import {applyMiddleware, combineReducers, compose, createStore} from 'redux'
import {TypedUseSelectorHook, useSelector} from "react-redux";
import {LoginState, LoginModule} from "../routes/Login/LoginModule";

export type ApplicationState = LoginState;

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

export const store = createStore(combineReducers({[LoginModule.name]: LoginModule.reducer}), {}, compose(...enhancers),);

// -------------------- Selectors --------------------

export const useTypedSelector: TypedUseSelectorHook<ApplicationState> = useSelector;
