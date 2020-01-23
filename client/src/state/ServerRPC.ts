// AUTO-GENERATED! Do not edit!
import {Action} from "redux";

export const RequestQuestionsType = 'RequestQuestions';
type RequestQuestionsReduxAction = Action<typeof RequestQuestionsType>;
export interface RequestQuestions extends Action<typeof RequestQuestionsType> {
    questionId: number;
    type: typeof RequestQuestionsType;
}

export type RequestQuestionsAction = RequestQuestions & RequestQuestionsReduxAction;

export interface QuestionEntry {
    questionId: number;
    questionTitle: string;
}

export const QuestionsResponseType = 'QuestionsResponse';
type QuestionsResponseReduxAction = Action<typeof QuestionsResponseType>;
export interface QuestionsResponse extends Action<typeof QuestionsResponseType> {
    questions: QuestionEntry[];
    type: typeof QuestionsResponseType;
}

export type QuestionsResponseAction = QuestionsResponse & QuestionsResponseReduxAction;

export const AnswerType = 'Answer';
type AnswerReduxAction = Action<typeof AnswerType>;

export interface Answer extends Action<typeof AnswerType> {
    answer: string;
    questionId: number;
    type: typeof AnswerType;
}

export type AnswerAction = Answer & AnswerReduxAction;

export function createAnswerAction(arg: Answer): AnswerAction {
    return {
        ...arg,
        type: AnswerType,
    }
}

export const QuestionType = 'Question';
type QuestionReduxAction = Action<typeof QuestionType>;

export interface Question extends Action<typeof QuestionType> {
    code: string;
    input: string;
    questionId: number;
    title: string;
    type: typeof QuestionType;
}

export type QuestionAction = Question & QuestionReduxAction;
