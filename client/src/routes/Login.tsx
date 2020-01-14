import React, {useState} from "react";
import axios from "axios";
import {IStackTokens, PrimaryButton, Spinner, SpinnerSize, Stack, TextField} from "office-ui-fabric-react";
import {useDispatch} from "react-redux";
import {loginSuccessfulAction} from "../state/State";

export const Login: React.FC = () => {
    const [user, setUser] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [busy, setBusy] = useState<boolean>(false);
    const [failed, setFailed] = useState(false);

    const dispatch = useDispatch();

    const submitAction = async () => {
        setBusy(true);
        setFailed(false);
        try {
            // Perform the fetch
            const bodyFormData = new FormData();
            bodyFormData.set("name", user);
            bodyFormData.set("password", password);
            let response = await axios({
                method: 'post',
                url: '/api/login',
                data: bodyFormData,
                headers: {'Content-Type': 'multipart/form-data' }
            });
            setBusy(false);
            dispatch(loginSuccessfulAction(response.data, response.data));
        } catch (e) {
            setBusy(false);
            setFailed(true);
        }
    };

    const error = failed ? "Invalid credentials" : "";

    const tokens: IStackTokens = {
        childrenGap: 10,
    };
    return <Stack style={{height: "100%"}} verticalAlign="center" horizontalAlign="center">
        <Stack className="ms-depth-8" style={{width: "17em", padding: "20px 30px"}} tokens={tokens}>
            <div className="ms-fontSize-42">Login</div>
            <TextField label="Name" value={user} onChange={
                (e, v) => setUser(v ?? '')
            }/>
            <TextField label="Password" type="password" value={password} errorMessage={error} onChange={(
                e, v) => setPassword(v ?? '')
            }/>
            <Stack horizontal verticalAlign="center" tokens={tokens}>
                <PrimaryButton text="Login" disabled={busy} onClick={submitAction}/>
                {busy ? <Spinner size={SpinnerSize.medium}/> : null}
            </Stack>
        </Stack>
    </Stack>;
};
