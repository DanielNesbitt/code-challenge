// AUTO-GENERATED! Do not edit!
import {Action} from "redux";

export type SocketAction = {
    meta: {
        socket: true
    }
}

export interface QuestionEntry {
    correct: boolean;
    questionId: number;
    title: string;
}

export const QuestionsResponseType = 'QuestionsResponse';
export interface QuestionsResponse {
    questions: QuestionEntry[];
}
export type QuestionsResponseAction = QuestionsResponse & Action<typeof QuestionsResponseType>;

export const RequestQuestionType = 'RequestQuestion';
export interface RequestQuestion {
    questionId: number;
}
export type RequestQuestionAction = RequestQuestion & Action<typeof RequestQuestionType>;
export function createRequestQuestionAction(arg: RequestQuestion): RequestQuestionAction & SocketAction {
    return {
        ...arg,
        type: RequestQuestionType,
        meta: {
            socket: true
        }
    }
}

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

export const AnswerResultType = 'AnswerResult';
export interface AnswerResult {
    answer: string;
    correct: boolean;
    questionId: number;
}
export type AnswerResultAction = AnswerResult & Action<typeof AnswerResultType>;