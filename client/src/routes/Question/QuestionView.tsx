import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {useParams} from "react-router";
import {
    ITextFieldProps,
    Label,
    MessageBar,
    MessageBarType,
    PrimaryButton,
    Stack,
    TextField,
    IRenderFunction, Icon
} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {createAnswerAction, createRequestQuestionAction} from "../../state/ServerRPC";
import {PageSpinner} from "../../components/PageSpinner";
import {questionSelector, resultSelector} from "./QuestionModule";
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

const goodAnswer = <Icon iconName={"Checkmark"} styles={{root: {marginBottom: -3, color: "green"}}}/>;
const badAnswer = <Icon iconName={"Cancel"} styles={{root: {marginBottom: -3, color: "red"}}}/>;

export const QuestionView: React.FC = () => {
    const question = useTypedSelector(questionSelector);
    const result = useTypedSelector(resultSelector);
    const {id: idFromRouter} = useParams<QuestionRouteParams>();
    const [answer, setAnswer] = useState("");
    const [waiting, setWaiting] = useState(false);
    const [failed, setFailed] = useState(false);

    const dispatch = useDispatch();
    const dispatchAnswer = () => {
        setWaiting(true);
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

    useEffect(() => {
        if (!!result && waiting) {
            setWaiting(answer !== result.answer);
        }
    }, [result, answer, waiting]);

    const resultIcon = result && result.correct ? badAnswer : goodAnswer;
    const renderLabel: IRenderFunction<ITextFieldProps> = (props, defaultRender) => {
        return (
            <Stack horizontal verticalAlign="center" tokens={{childrenGap: 6}}>
                <span>{defaultRender!(props)}</span>
                {!result ? null : resultIcon}
            </Stack>
        );
    };

    return <Stack className="ms-depth-8" style={{padding: "20px"}} tokens={defaultChildGap}>
        {failed && idFromRouter ? <NotFoundError id={idFromRouter}/> : null}
        <div style={{width: "100%"}}>
            <Label>Question</Label>
            {questionIsLoaded
                ? <MarkdownView markdown={question!.text}/>
                : <PageSpinner/>
            }
        </div>
        <TextField label="Answer" value={answer} onRenderLabel={renderLabel} onChange={fieldInput(setAnswer)}/>
        <PrimaryButton text="Submit" disabled={waiting} onClick={dispatchAnswer}/>
    </Stack>;
};
