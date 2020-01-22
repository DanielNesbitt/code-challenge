import {ReduxModule} from "../../state/Module";
import {ApplicationState} from "../../state/State";
import {Question as QuestionAction, QuestionType} from "../../state/ServerRPC";

const NAME = 'Question';

export type QuestionDTO = Omit<QuestionAction, 'type'>

export type State = {
    question?: QuestionDTO;
}

export const questionSelector = (state: ApplicationState): (QuestionDTO | undefined) => state[NAME].question;

type Actions = QuestionAction;

export type QuestionState = Record<typeof NAME, State>;

const defaultQuestion: QuestionDTO = {
    questionId: 0,
    title: "Fibonacci",
    code: `
private fun mysteriousCode(foo: Int, bar: Int, baz: Int): Int {
    return when (foo) {
        0 -> baz
        1 -> bar
        else -> {
            fib(foo - 1, bar + baz, bar)
        }
    }
}
    `,
    input: "5",
};

export const QuestionModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = { question: defaultQuestion }, action: Actions) => {
        switch (action.type) {
            case QuestionType: {
                return {
                    ...state,
                    question: action
                };
            }
        }
        return state;
    }
};
