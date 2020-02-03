import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {useParams} from "react-router";
import {Label, MessageBar, MessageBarType, PrimaryButton, Stack, TextField} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {createAnswerAction, createRequestQuestionAction} from "../../state/ServerRPC";
import {PageSpinner} from "../../components/PageSpinner";
import {questionSelector} from "./QuestionModule";
import {MarkdownView} from "../../components/MarkdownView";
import {fieldInput} from "../../util/EventUtils";
import {defaultChildGap} from "../../util/StackUtils";

type QuestionRouteParams = { id?: string; }

type NotFoundProps = { id: string }
const NotFoundError: React.FC<NotFoundProps> = ({id}) => (
    <MessageBar messageBarType={MessageBarType.error}>
        Question not found for id: {id}
    </MessageBar>
);


export const QuestionView: React.FC = () => {
    const question = useTypedSelector(questionSelector);
    const { id: idFromRouter } = useParams<QuestionRouteParams>();
    const [answer, setAnswer] = useState("");
    const [failed, setFailed] = useState(false);

    const dispatch = useDispatch();
    const dispatchAnswer = () => {
        // TODO Prevent multiple dispatch?
        if (question) {
            dispatch(createAnswerAction({answer, questionId: question.questionId}));
        }
    };

    const id = !!idFromRouter ? parseInt(idFromRouter) : NaN;
    const questionIsLoaded = !!question && question.questionId === id;

    useEffect(() => {
        if (!questionIsLoaded) {
            try {
                dispatch(createRequestQuestionAction({questionId: id}));
            } catch (e) {
                setFailed(true);
            }
        }
    }, [dispatch, questionIsLoaded, id]);


    return <Stack className="ms-depth-8" style={{padding: "20px"}} tokens={defaultChildGap}>
        {failed && idFromRouter ? <NotFoundError id={idFromRouter}/> : null}
        <div style={{width: "100%"}}>
            <Label>Question</Label>
            {questionIsLoaded
                ? <MarkdownView markdown={question!.text}/>
                : <PageSpinner/>
            }
        </div>
        <TextField label="Answer" value={answer} onChange={fieldInput(setAnswer)}/>
        <PrimaryButton text="Submit" onClick={dispatchAnswer}/>
    </Stack>;
};
