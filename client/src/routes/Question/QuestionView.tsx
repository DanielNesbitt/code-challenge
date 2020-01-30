import React, {useEffect, useState} from "react";
import {Label, MessageBar, MessageBarType, PrimaryButton, Stack, TextField} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {createAnswerAction, createRequestQuestionAction} from "../../state/ServerRPC";
import {PageSpinner} from "../../components/PageSpinner";
import {questionSelector} from "./QuestionModule";
import {MarkdownView} from "../../components/MarkdownView";
import {fieldInput} from "../../util/EventUtils";
import {useDispatch} from "react-redux";
import {defaultChildGap} from "../../util/StackUtils";
import {useParams} from "react-router";

type QuestionRouteParams = { id?: string; }

type NotFoundProps = { id: string }
const NotFoundError: React.FC<NotFoundProps> = ({id}) => (
    <MessageBar messageBarType={MessageBarType.error}>
        Question not found for id: {id}
    </MessageBar>
);


export const QuestionView: React.FC = () => {
    const question = useTypedSelector(questionSelector);
    const [answer, setAnswer] = useState("");
    const [failed, setFailed] = useState(false);

    const dispatch = useDispatch();
    const dispatchAnswer = () => {
        // TODO Prevent multiple dispatch?
        if (question) {
            dispatch(createAnswerAction({answer, questionId: question.questionId}));
        }
    };

    const params = useParams<QuestionRouteParams>();
    const {id} = params;

    useEffect(() => {
        if (id) {
            try {
                const questionId = parseInt(id);
                dispatch(createRequestQuestionAction({questionId: questionId}));
            } catch (e) {
                setFailed(true);
            }
        }
    }, [id, dispatch]);

    return <Stack className="ms-depth-8" style={{padding: "20px"}} tokens={defaultChildGap}>
        {failed && id ? <NotFoundError id={id}/> : null}
        <div style={{width: "100%"}}>
            <Label>Question</Label>
            {question
                ? <MarkdownView markdown={question?.text}/>
                : <PageSpinner/>
            }
        </div>
        <TextField label="Answer" value={answer} onChange={fieldInput(setAnswer)}/>
        <PrimaryButton text="Submit" onClick={dispatchAnswer}/>
    </Stack>;
};
