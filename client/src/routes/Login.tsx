import React, {useState} from "react";
import axios from "axios";
import {PrimaryButton, Stack, TextField} from "office-ui-fabric-react";
import {useDispatch} from "react-redux";
import {loginSuccessfulAction} from "../state/State";

export const Login: React.FC = () => {
    const [user, setUser] = useState<string | undefined>("");
    const [password, setPassword] = useState<string | undefined>("");
    const [busy, setBusy] = useState<boolean>(false);

    const dispatch = useDispatch();

    const submitAction = () => {
        setBusy(true);
        // Perform the fetch
        const foo = async () => {
            await axios.post<string>('/login', {
                name: user,
                password: password
            }).then(response => {
                setBusy(false);
                dispatch(loginSuccessfulAction(response.data, response.data))
            }).catch(error =>
                setBusy(false)
            );
        };
    };

    return (
        <Stack>
            <TextField label="Name" value={user} onChange={(e, v) => setUser(v)}/>
            <TextField label="Password" type="password" value={password} onChange={(e, v) => setPassword(v)}/>
            <PrimaryButton label="Login" disabled={busy} onClick={submitAction}/>
        </Stack>
    );
};
