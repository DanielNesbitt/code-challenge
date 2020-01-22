import React, {KeyboardEvent, useState} from "react";
import axios, {AxiosResponse} from "axios";
import {
    DefaultButton,
    IStackTokens,
    Label,
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
    const [createdMsg, setCreatedMsg] = useState<string> ("");

    const dispatch = useDispatch();

    const dispatchLogin = (response: AxiosResponse) => {
        dispatch(loginSuccessfulAction(response.data, response.data))
    };

    const showMessage = (response: AxiosResponse) => {
        setCreatedMsg(response.data);
    };

    const doLoginAction = (url: string, onComplete: Function) => {
        return async () => {
            setBusy(true);
            setErrorMsg("");
            setCreatedMsg("");
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
                onComplete(response);
            } catch (e) {
                setBusy(false);
                if (e.response.status === 401) {
                    setErrorMsg("Invalid credentials.")
                } else {
                    setErrorMsg(e.response.data);
                }
            }
        };
    };


    const submitAction = doLoginAction("/api/login", dispatchLogin);
    const createAction = doLoginAction("/api/newGroup", showMessage);

    const tokens: IStackTokens = {
        childrenGap: 10,
    };

    const onEnter = (e: KeyboardEvent) => {
        if (e.keyCode === 13) {
            // noinspection JSIgnoredPromiseFromCall
            submitAction()
        }
    };

    return <Stack style={{height: "100%"}} verticalAlign="center" horizontalAlign="center">
        <Stack className="ms-depth-8" style={{width: "17em", padding: "20px 30px"}} tokens={tokens}>
            <div className="ms-fontSize-42">Login</div>
            <TextField label="Name" value={user} onChange={
                (e, v) => setUser(v ?? '')
            } onKeyUp={onEnter}/>
            <TextField label="Password" type="password" value={password} errorMessage={errorMsg} onChange={(
                e, v) => setPassword(v ?? '')
            } onKeyUp={onEnter}/>
            <Stack horizontal verticalAlign="center" tokens={tokens}>
                <PrimaryButton text="Login" disabled={busy} onClick={submitAction}/>
                <DefaultButton text="Create User" disabled={busy} onClick={createAction}/>
                {busy ? <Spinner size={SpinnerSize.medium}/> : null}
            </Stack>
            {createdMsg ? <Label>{createdMsg}</Label> : null}
        </Stack>
    </Stack>;
};
