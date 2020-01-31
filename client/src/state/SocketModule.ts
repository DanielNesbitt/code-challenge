import {ReduxModule} from "./Module";
import {Action} from "redux";
import {ApplicationState} from "./State";

const NAME = 'Socket';

export enum SocketStatus {
    Not_Established = 'Not_Established',
    Connecting = 'Connecting',
    Connected = 'Connected',
    Disconnected = 'Disconnected'
}

export type State = {
    status: SocketStatus
}

const SocketUpdateType = 'ConnectionUpdate';

interface SocketStatusUpdate extends Action<typeof SocketUpdateType> {
    type: typeof SocketUpdateType;
    status: SocketStatus;
}

export const socketStatusAction = (status: SocketStatus): SocketStatusUpdate => ({
    type: SocketUpdateType,
    status,
});

type Actions = SocketStatusUpdate;

export const socketStatusSelector = (state: ApplicationState): SocketStatus => state[NAME].status;
export type SocketState = Record<typeof NAME, State>;

export const SocketModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = {status: SocketStatus.Not_Established}, action: Actions) => {
        if (action.type === SocketUpdateType) {
            return {
                status: action.status
            };
        }
        return state;
    }
};
