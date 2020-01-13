import React, {useState} from "react";
import {PrimaryButton, Stack, TextField} from "office-ui-fabric-react";

export const Login: React.FC = () => {
    const [user, setUser] = useState<string | undefined>("");
    const [password, setPassword] = useState<string | undefined>("");
    const [busy, setBusy] = useState<boolean>(false);

    const submitAction = () => {
        setBusy(true);
        // Perform the fetch
        async () => {
            // await fetch({method="POST", url="/login", })
            setBusy(false);
        };
    };

    return (
        <Stack>
            <TextField label="Name" value={user} onChange={(e, v) => setUser(v)}/>
            <TextField label="Password" type="password" value={password} onChange={(e, v) => setPassword(v)}/>
            <PrimaryButton label="Login" disabled={busy} onClick={submitAction} />
        </Stack>
    );
};
