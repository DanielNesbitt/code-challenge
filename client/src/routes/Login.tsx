import React, {useState} from "react";
import axios from "axios";
import {IStackTokens, PrimaryButton, Stack, TextField} from "office-ui-fabric-react";
import {useDispatch} from "react-redux";
import {loginSuccessfulAction} from "../state/State";
import './Login.css';

export const Login: React.FC = () => {
    const [user, setUser] = useState<string | undefined>("");
    const [password, setPassword] = useState<string | undefined>("");
    const [busy, setBusy] = useState<boolean>(false);
    const [failed, setFailed] = useState(false);

    const dispatch = useDispatch();

    const submitAction = async () => {
        setBusy(true);
        setFailed(false);
        try {
            // Perform the fetch
            let response = await axios.post<string>('/api/login', {
                name: user,
                password: password
            });
            setBusy(false);
            dispatch(loginSuccessfulAction(response.data, response.data));
        } catch (e) {
            setBusy(false);
            setFailed(true);
        }
    };

    const error = failed ? "Invalid credentials" : "";
    const configureStackTokens: IStackTokens = {childrenGap: 10};

    return <div className="Login">
        <div className="LoginForm ms-depth-8">
            <div className="ms-fontSize-42">Login</div>
            <Stack tokens={configureStackTokens}>
                <TextField label="Name" value={user} onChange={
                    (e, v) => setUser(v)
                }/>
                <TextField label="Password" type="password" value={password} errorMessage={error} onChange={(
                    e, v) => setPassword(v)
                }/>
                <PrimaryButton text="Login" disabled={busy} onClick={submitAction}/>
            </Stack>
        </div>
    </div>;
};
