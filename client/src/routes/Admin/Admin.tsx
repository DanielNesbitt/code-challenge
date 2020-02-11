import React, {useEffect, useState} from "react";
import {DetailsList, IColumn, SelectionMode, Stack} from "office-ui-fabric-react";
import axios from "axios";

const columns: IColumn[] = [
    {
        key: 'name',
        name: 'Name',
        minWidth: 200,
        maxWidth: 200,
        onRender: (item) => {
            return item.name;
        }
    } as IColumn,
        {
            key: 'question',
            name: 'Question',
            minWidth: 100,
            maxWidth: 100,
            onRender: (item) => {
                return item.question;
            }
    } as IColumn,
        {
            key: 'correct',
            name: 'Is Correct',
            minWidth: 64,
            maxWidth: 64,
            onRender: (item) => {
                 return item.correct ? "yes" : null;
            }

    } as IColumn,
    {
        key: 'answer',
       name: 'Submitted',
       minWidth: 200,
       maxWidth: 500,
       onRender: (item) => {
           return item.answer;
       }
    } as IColumn,
];

export const Admin: React.FC = () => {
    const [answers, setAnswers] = useState<any[] | undefined >([]);

    useEffect(() => {
            axios("/api/superSecretAdminRoute").then(response => {
                if (!!response.data) {
                    setAnswers(response.data.userAnswers);
                }
            });
        }, []);
   return <Stack horizontal horizontalAlign="center" className="ms-depth-8" style={{margin: "20px"}}>

     <div style={{width: "100%", padding: "10px"}}>

        <DetailsList
            selectionMode={SelectionMode.none}
            items={answers ? answers : []}
            columns={columns}
        />
        </div>

    </Stack>
};
