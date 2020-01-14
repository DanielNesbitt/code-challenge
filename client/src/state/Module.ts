import {Action, AnyAction, Reducer} from "redux";

export type ReduxModule<S = unknown, A extends Action = AnyAction> = {
    name: string;
    reducer: Reducer<S, A>;
};
