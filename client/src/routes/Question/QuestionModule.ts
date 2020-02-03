import {ReduxModule} from "../../state/Module";
import {ApplicationState} from "../../state/State";
import {Question, QuestionAction, QuestionType} from "../../state/ServerRPC";

const NAME = 'Question';

export type State = {
    question?: Question;
}

export const questionSelector = (state: ApplicationState): (Question | undefined) => state[NAME].question;

type Actions = QuestionAction;

export type QuestionState = Record<typeof NAME, State>;

const defaultQuestion: Question = {
    questionId: 0,
    title: "Fibonacci",
    text: `
\`\`\`kotlin
private fun mysteriousCode(foo: Int, bar: Int, baz: Int): Int {
    return when (foo) {
        0 -> baz
        1 -> bar
        else -> {
            fib(foo - 1, bar + baz, bar)
        }
    }
}
\`\`\`

What is the output of this function given foo = 1, bar = 2, and baz = 3?
    `,
};

export const QuestionModule: ReduxModule<State, Actions> = {
    name: NAME,
    reducer: (state: State = { question: undefined }, action: Actions) => {
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
