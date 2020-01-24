// AUTO-GENERATED! Do not edit!
import {Action} from "redux";

export const RequestQuestionsType = 'RequestQuestions';
type RequestQuestionsReduxAction = Action<typeof RequestQuestionsType>;
export interface RequestQuestions {
    questionId: number;
}
export type RequestQuestionsAction = RequestQuestions & RequestQuestionsReduxAction;

export interface QuestionEntry {
    questionId: number;
    questionTitle: string;
}

export const QuestionsResponseType = 'QuestionsResponse';
type QuestionsResponseReduxAction = Action<typeof QuestionsResponseType>;

export interface QuestionsResponse {
    questions: QuestionEntry[];
}

export type QuestionsResponseAction = QuestionsResponse & QuestionsResponseReduxAction;

export const QuestionType = 'Question';
type QuestionReduxAction = Action<typeof QuestionType>;

export interface Question {
    questionId: number;
    text: string;
    title: string;
}

export type QuestionAction = Question & QuestionReduxAction;

export const AnswerType = 'Answer';
type AnswerReduxAction = Action<typeof AnswerType>;

export interface Answer {
    answer: string;
    questionId: number;
}

export type AnswerAction = Answer & AnswerReduxAction;

export function createAnswerAction(arg: Answer): AnswerAction {
    return {
        ...arg,
        type: AnswerType,
    }
}
