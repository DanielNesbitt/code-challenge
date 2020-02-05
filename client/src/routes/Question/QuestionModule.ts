import {ReduxModule} from "../../state/Module";
import {ApplicationState} from "../../state/State";
import {
    AnswerResult,
    AnswerResultAction,
    AnswerResultType,
    Question,
    QuestionAction,
    QuestionType
} from "../../state/ServerRPC";

const NAME = 'Question';

export type State = {
    question?: Question;
    result?: AnswerResult;
}

export const questionSelector = (state: ApplicationState): (Question | undefined) => state[NAME].question;
export const resultSelector = (state: ApplicationState): (AnswerResult | undefined) => state[NAME].result;

type Actions = QuestionAction | AnswerResultAction;

export type QuestionState = Record<typeof NAME, State>;

export const QuestionModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = {}, action: Actions) => {
        switch (action.type) {
            case QuestionType: {
                return {
                    ...state,
                    question: action
                };
            }
            case AnswerResultType: {
                return {
                    ...state,
                    result: action
                }
            }
        }
        return state;
    }
};
