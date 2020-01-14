// AUTO-GENERATED! Do not edit!
export const RequestQuestionType = 'RequestQuestion';

interface RequestQuestion {
    questionId: number;
    type: typeof RequestQuestionType;
}

export const QuestionType = 'Question';

interface Question {
    question: string;
    questionId: number;
    type: typeof QuestionType;
}

export const AnswerType = 'Answer';

interface Answer {
    answer: string;
    options: { [key: string]: string };
    questionId: number;
    type: typeof AnswerType;
}
