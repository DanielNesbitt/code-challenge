// AUTO-GENERATED! Do not edit!
import {Action} from "redux";

export const RequestQuestionsType = 'RequestQuestions';
export interface RequestQuestions extends Action<typeof RequestQuestionsType> {
    questionId: number;
    type: typeof RequestQuestionsType;
}

export interface QuestionEntry {
    questionId: number;
    questionTitle: string;
}

export const QuestionsResponseType = 'QuestionsResponse';
export interface QuestionsResponse extends Action<typeof QuestionsResponseType> {
    questions: QuestionEntry[];
    type: typeof QuestionsResponseType;
}

export const AnswerType = 'Answer';
export interface Answer extends Action<typeof AnswerType> {
    answer: string;
    questionId: number;
    type: typeof AnswerType;
}