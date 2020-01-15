import {Action, AnyAction, Dispatch, Middleware, MiddlewareAPI} from "redux";
import {ApplicationState} from "./State";
import {LoginSuccessfulType} from "../routes/Login/LoginModule";

type SocketMeta = { socket: true }

const createSocket = (): WebSocket => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    return new WebSocket(`${protocol}//${window.location.host}/api/ws`)
};

export const socketMiddleware: Middleware = (api: MiddlewareAPI<Dispatch, ApplicationState>) => (next: Dispatch<Action>) => {
    let socket: WebSocket | undefined;
    let user: string | null = null;
    return (action: AnyAction) => {
        if (action.type === LoginSuccessfulType && user !== action.user) {
            if (socket) {
                socket?.close();
            }

            socket = createSocket();
            socket.onopen = ev => {
                console.log('WS Ready')
            };
            socket.onmessage = ev => {
                const action = JSON.parse(ev.data);
                if (action.type) {
                    api.dispatch(action);
                }
            };
            socket.onerror = e => console.log('errored', e);

            user = action.user;
        }
        if (action.meta?.socket) {
            socket?.send(JSON.stringify(action));
        }
        return next(action);
    };
};
