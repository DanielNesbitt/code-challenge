import {Action, applyMiddleware, compose, createStore} from 'redux'
import {TypedUseSelectorHook, useSelector} from "react-redux";

export type ApplicationState = {
    user?: string;
}

export const LoginSuccessfulType = 'LoginSuccessful';
interface LoginSuccessful extends Action<typeof LoginSuccessfulType> {
    type: typeof LoginSuccessfulType;
    id: string;
    name: string;
}

type Actions = LoginSuccessful;

type LoginSuccessfulAction = Action<typeof LoginSuccessfulType>

export function loginSuccessfulAction(id: string, name: string): LoginSuccessful {
    return {
        type: LoginSuccessfulType,
        id,
        name,
    };
}

const reducer = (state: ApplicationState = {}, action: Actions) => {
    switch (action.type) {
        case LoginSuccessfulType: {
            return {
                ...state,
                user: action.name
            };
        }
    }
    return state;
};

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

export const store = createStore(reducer, {}, compose(...enhancers),);

// -------------------- Selectors --------------------

export const useTypedSelector: TypedUseSelectorHook<ApplicationState> = useSelector;
