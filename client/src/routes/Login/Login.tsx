import React, {useState} from "react";
import axios from "axios";
import {
    DefaultButton,
    IStackTokens,
    PrimaryButton,
    Spinner,
    SpinnerSize,
    Stack,
    TextField
} from "office-ui-fabric-react";
import {useDispatch} from "react-redux";
import {loginSuccessfulAction} from "./LoginModule";

export const Login: React.FC = () => {
    const [user, setUser] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [busy, setBusy] = useState<boolean>(false);
    const [errorMsg, setErrorMsg] = useState<string>("");

    const dispatch = useDispatch();

    const doLoginAction = (url: string) => {
        return async() => {
            setBusy(true);
            setErrorMsg("");
            try {
                // Perform the fetch
                const bodyFormData = new FormData();
                bodyFormData.set("name", user);
                bodyFormData.set("password", password);
                let response = await axios({
                    method: 'post',
                    url: url,
                    data: bodyFormData,
                    headers: {'Content-Type': 'multipart/form-data' }
                });
                setBusy(false);
                let foo = dispatch(loginSuccessfulAction(response.data, response.data));
            } catch (e) {
                setBusy(false);
                setErrorMsg(e.response.data);
            }
        };
    };

    const submitAction = doLoginAction("/api/login");
    const createAction = doLoginAction("/api/newGroup");


    const tokens: IStackTokens = {
        childrenGap: 10,
    };
    return <Stack style={{height: "100%"}} verticalAlign="center" horizontalAlign="center">
        <Stack className="ms-depth-8" style={{width: "17em", padding: "20px 30px"}} tokens={tokens}>
            <div className="ms-fontSize-42">Login</div>
            <TextField label="Name" value={user} onChange={
                (e, v) => setUser(v ?? '')
            }/>
            <TextField label="Password" type="password" value={password} errorMessage={errorMsg} onChange={(
                e, v) => setPassword(v ?? '')
            }/>
            <Stack horizontal verticalAlign="center" tokens={tokens}>
                <PrimaryButton text="Login" disabled={busy} onClick={submitAction}/>
                <DefaultButton text ="Create User" disabled={busy} onClick={createAction}/>
                {busy ? <Spinner size={SpinnerSize.medium}/> : null}
            </Stack>
        </Stack>
    </Stack>;
};
