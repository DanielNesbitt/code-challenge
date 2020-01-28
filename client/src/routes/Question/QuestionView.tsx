import React, {useState} from "react";
import {Label, PrimaryButton, Stack, TextField} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {createAnswerAction, Question} from "../../state/ServerRPC";
import {PageSpinner} from "../../components/PageSpinner";
import {questionSelector} from "./QuestionModule";
import {MarkdownView} from "../../components/MarkdownView";
import {fieldInput} from "../../util/EventUtils";
import {useDispatch} from "react-redux";
import {defaultChildGap} from "../../util/StackUtils";

export const QuestionView: React.FC = () => {
    const question = useTypedSelector(questionSelector);
    const [answer, setAnswer] = useState("");

    const dispatch = useDispatch();
    const dispatchAnswer = () => {
        // TODO Prevent multiple dispatch?
        if (question) {
            dispatch(createAnswerAction({answer, questionId: question.questionId}));
        }
    };

    return <Stack className="ms-depth-8" style={{padding: "20px"}} tokens={defaultChildGap}>
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
