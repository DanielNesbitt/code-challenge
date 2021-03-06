import React, {useState} from "react";
import axios, {AxiosResponse} from "axios";
import {DefaultButton, Label, PrimaryButton, Spinner, SpinnerSize, Stack, TextField} from "office-ui-fabric-react";
import {useDispatch} from "react-redux";
import {loginSuccessfulAction} from "./LoginModule";
import {fieldInput, onEnter} from "../../util/EventUtils";
import {defaultChildGap} from "../../util/StackUtils";

export const Login: React.FC = () => {
    const [user, setUser] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [busy, setBusy] = useState<boolean>(false);
    const [errorMsg, setErrorMsg] = useState<string>("");
    const [createdMsg, setCreatedMsg] = useState<string>("");

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
    const createAction = doLoginAction("/api/newUser", showMessage);

    const tokens = defaultChildGap;

    const onKeyEnter = onEnter(submitAction);

    return <Stack style={{height: "100%"}} verticalAlign="center" horizontalAlign="center">
        <Stack className="ms-depth-8" style={{width: "17em", padding: "20px 30px"}}
               tokens={tokens}>
            <div className="ms-fontSize-42">Login</div>
            <TextField label="Name" value={user} onChange={fieldInput(setUser)}
                       onKeyUp={onKeyEnter}/>
            <TextField label="Password" type="password" value={password}
                       errorMessage={errorMsg} onChange={fieldInput(setPassword)}
                       onKeyUp={onKeyEnter}/>
            <Stack horizontal verticalAlign="center" tokens={tokens}>
                <PrimaryButton text="Login" disabled={busy} onClick={submitAction}/>
                <DefaultButton text="Create User" disabled={busy} onClick={createAction}/>
                {busy ? <Spinner size={SpinnerSize.medium}/> : null}
            </Stack>
            {createdMsg ? <Label>{createdMsg}</Label> : null}
        </Stack>
    </Stack>;
};
