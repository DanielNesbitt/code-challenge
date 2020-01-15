import {ReduxModule} from "../../state/Module";
import {ApplicationState} from "../../state/State";
import {QuestionEntry, QuestionsResponse, QuestionsResponseType} from "../../state/ServerRPC";

const NAME = 'Home';

export type State = {
    questions?: QuestionEntry[];
}

export const questionsSelector = (state: ApplicationState): (QuestionEntry[] | undefined) => state[NAME].questions;

type Actions = QuestionsResponse;

export type HomeState = Record<typeof NAME, State>;

export const HomeModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = {}, action: Actions) => {
        switch (action.type) {
            case QuestionsResponseType: {
                return {
                    ...state,
                    questions: action.questions
                };
            }
        }
        return state;
    }
};
