import React from "react";
import {DetailsList, IColumn} from "office-ui-fabric-react";
import {questionsSelector} from "./HomeModule";
import {QuestionEntry} from "../../state/ServerRPC";
import {useTypedSelector} from "../../state/Store";

const columns: IColumn[] = [
    {
        key: 'question_state',
        name: 'Answered',
    } as IColumn,
    {
        key: 'question',
        name: 'Question',
        data: 'questionTitle',
    } as IColumn
];

export const Home: React.FC = () => {
    const questions: QuestionEntry[] | undefined = useTypedSelector(questionsSelector);
    return <div>
        {questions ?
            <DetailsList
                items={questions}
                columns={columns}
            />
            : null}
    </div>;
};
