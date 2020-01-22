import React from "react";
import {useTypedSelector} from "../../state/Store";
import {Stack} from "office-ui-fabric-react";
import {QuestionDTO, questionSelector} from "./QuestionModule";

export const Question: React.FC = () => {
    const question: QuestionDTO | undefined = useTypedSelector(questionSelector);
    return <Stack horizontal horizontalAlign="center" className="ms-depth-8" style={{margin: "20px"}}>
        <div style={{width: "100%", padding: "10px"}}>
            {question ?
               <pre>{question?.code}</pre>
                : null}
        </div>
    </Stack>;
};
