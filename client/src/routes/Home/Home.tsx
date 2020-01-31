import React from "react";
import {Link} from "react-router-dom";
import {DetailsList, IColumn, SelectionMode, Stack} from "office-ui-fabric-react";
import {useTypedSelector} from "../../state/Store";
import {QuestionEntry} from "../../state/ServerRPC";
import {questionsSelector} from "./HomeModule";

const columns: IColumn[] = [
    {
        key: 'question_state',
        name: 'Answered',
        maxWidth: 100,
    } as IColumn,
    {
        key: 'question',
        name: 'Question',
        onRender: (item) => {
            const question = item as QuestionEntry;
            return <Link to={`/question/${question.questionId}`}>{question.title}</Link>
        }
    } as IColumn
];

export const Home: React.FC = () => {
    const questions = useTypedSelector(questionsSelector);

    return <Stack horizontal horizontalAlign="center" className="ms-depth-8" style={{margin: "20px"}}>
        <div style={{width: "100%", padding: "10px"}}>
            {questions ?
                <DetailsList
                    selectionMode={SelectionMode.none}
                    items={questions}
                    columns={columns}
                />
                : null}
        </div>
    </Stack>;
};
