import {Action, AnyAction, Dispatch, Middleware, MiddlewareAPI} from "redux";
import {ApplicationState} from "./State";
import {LoginSuccessfulType} from "../routes/Login/LoginModule";
import {SocketStatus, socketStatusAction} from "./SocketModule";

const createSocket = (): WebSocket => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    return new WebSocket(`${protocol}//${window.location.host}/api/ws`)
};

export const socketMiddleware: Middleware = (api: MiddlewareAPI<Dispatch, ApplicationState>) => (next: Dispatch<Action>) => {
    let socket: WebSocket | undefined;
    let user: string | null = null;

    const updateStatus = (status: SocketStatus) => {
        api.dispatch(socketStatusAction(status))
    };

    return (action: AnyAction) => {
        if (action.type === LoginSuccessfulType && user !== action.user) {
            if (socket) {
                socket?.close();
            }

            socket = createSocket();
            updateStatus(SocketStatus.Connecting);

            socket.onopen = () => {
                updateStatus(SocketStatus.Connected);
            };

            socket.onmessage = ev => {
                const action = JSON.parse(ev.data);
                if (action.type) {
                    api.dispatch(action);
                }
            };

            socket.onerror = e => console.log('Socket errored:', e);

            socket.onclose = e => {
                console.log('Socket closed:', e.code);
                updateStatus(SocketStatus.Disconnected);
            };

            user = action.user;
        }

        if (action.meta?.socket) {
            if (!!socket && socket.readyState === 1) {
                socket?.send(JSON.stringify(action));
            } else {
                console.error('Socket action dispatched without an open socket.')
            }
        }
        return next(action);
    };
};
