// AUTO-GENERATED! Do not edit!
import {Action} from "redux";

export type SocketAction = {
    meta: {
        socket: true
    }
}

export interface QuestionEntry {
    questionId: number;
    questionTitle: string;
}

export const QuestionsResponseType = 'QuestionsResponse';
export interface QuestionsResponse {
    questions: QuestionEntry[];
}
export type QuestionsResponseAction = QuestionsResponse & Action<typeof QuestionsResponseType>;

export const QuestionType = 'Question';
export interface Question {
    questionId: number;
    text: string;
    title: string;
}
export type QuestionAction = Question & Action<typeof QuestionType>;

export const AnswerType = 'Answer';
export interface Answer {
    answer: string;
    questionId: number;
}
export type AnswerAction = Answer & Action<typeof AnswerType>;
export function createAnswerAction(arg: Answer): AnswerAction & SocketAction {
    return {
        ...arg,
        type: AnswerType,
        meta: {
            socket: true
        }
    }
}
