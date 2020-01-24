import React from "react";
import ReactMarkdown from "react-markdown";
import {Stack} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {Question} from "../../state/ServerRPC";
import {PageSpinner} from "../../components/PageSpinner";
import {questionSelector} from "./QuestionModule";

export const QuestionView: React.FC = () => {
    const question: Question | undefined = useTypedSelector(questionSelector);
    return <Stack horizontal horizontalAlign="center" className="ms-depth-8" style={{margin: "20px"}}>
        <div style={{width: "100%", padding: "10px"}}>
            {question
                ? <ReactMarkdown source={question?.text}/>
                : <PageSpinner/>
            }
        </div>
    </Stack>;
};
