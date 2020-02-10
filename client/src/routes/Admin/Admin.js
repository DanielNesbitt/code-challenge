import React, {useEffect, useState} from "react";
import axios from "axios";

export const Admin = () => {
    const [answers, setAnswers] = useState(undefined);

    useEffect(() => {
            axios("/api/superSecretAdminRoute").then(response => {
                if (!!response.data) {
                    setAnswers(response.data.userAnswers);
                }
            });
        }, []);

    return <div style={{width: "100%", padding: "10px"}}>
        <table>
            <thead>
                <th>Name</th>
                <th>Question</th>
                <th>Submitted Answer</th>
                <th>Correct?</th>
            </thead>
            {answers !== undefined ? answers.map((answer) =>
                           <tr>
                               <td>{answer.name}</td>
                               <td>{answer.question}</td>
                               <td>{answer.answer}</td>
                               <td>{answer.correct ? "yes" : null}</td>
                           </tr>
                             ) : null}
        </table>
    </div>;
};
