import {Action} from "redux";
import {ReduxModule} from "../../state/Module";
import {ApplicationState} from "../../state/State";

const NAME = 'Login';

export type State = {
    user?: string;
}

const LoginSuccessfulType = 'LoginSuccessful';

interface LoginSuccessful extends Action<typeof LoginSuccessfulType> {
    type: typeof LoginSuccessfulType;
    id: string;
    name: string;
}

export function loginSuccessfulAction(id: string, name: string): LoginSuccessful {
    return {
        type: LoginSuccessfulType,
        id,
        name,
    };
}


type Actions = LoginSuccessful;

export const userSelector = (state: ApplicationState): (string | undefined) => state[NAME].user;
export type LoginState = Record<typeof NAME, State>;

export const LoginModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = {}, action: Actions) => {
        switch (action.type) {
            case LoginSuccessfulType: {
                return {
                    ...state,
                    user: action.name
                };
            }
        }
        return state;
    }
};
