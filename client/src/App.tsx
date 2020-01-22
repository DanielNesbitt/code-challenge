import React, {useEffect, useState} from "react";
import "./App.css";
import {Route, Router, Switch} from "react-router";
import {createBrowserHistory} from "history";
import {Home} from "./routes/Home/Home";
import {Question} from "./routes/Question/Question";
import {Provider, useDispatch} from "react-redux";
import {Login} from "./routes/Login/Login";
import {loginSuccessfulAction, userSelector} from "./routes/Login/LoginModule";
import {store, useTypedSelector} from "./state/Store";
import axios, {AxiosResponse} from "axios";
import {Spinner, SpinnerSize, Stack} from "office-ui-fabric-react";

const history = createBrowserHistory();

const Routing: React.FC = () => (<Router history={history}>
    <Switch>
        <Route path="/question/:id">
            <Question/>
        </Route>
        <Route path="/">
            <Home/>
        </Route>
    </Switch>
</Router>);

const AppSpinner: React.FC = () => (
    <Stack style={{height: "100%"}} verticalAlign="center">
        <Spinner size={SpinnerSize.large}/>
    </Stack>
);

const AppView: React.FC = () => {
    const [loaded, setLoaded] = useState(false);
    const dispatch = useDispatch();
    const dispatchLogin = (response: AxiosResponse) => {
        dispatch(loginSuccessfulAction(response.data, response.data))
    };

    useEffect(() => {
        axios("/api/user").then(response => {
            if (!!response.data) {
                dispatchLogin(response)
            }
            setLoaded(true);
        });
    });

    const user = useTypedSelector(userSelector);
    const Body = !!user ? <Routing/> : <Login/>;
    return (
        <div className="App">
            {user ? <h1>{user}</h1> : null}
            {loaded ? Body : <AppSpinner/>}
        </div>
    );
};

export const App: React.FC = () => (
    <Provider store={store}>
        <AppView/>
    </Provider>
);
