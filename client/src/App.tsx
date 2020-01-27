import React, {useEffect, useState} from "react";
import {Provider, useDispatch} from "react-redux";
import {Route, Router, Switch} from "react-router";
import {createBrowserHistory} from "history";
import axios, {AxiosResponse} from "axios";
import {store, useTypedSelector} from "./state/Store";
import {PageSpinner} from "./components/PageSpinner";
import {Home} from "./routes/Home/Home";
import {QuestionView} from "./routes/Question/QuestionView";
import {Login} from "./routes/Login/Login";
import {loginSuccessfulAction, userSelector} from "./routes/Login/LoginModule";
import "./App.css";
import {Breadcrumb, IBreadcrumbItem} from "office-ui-fabric-react";

const history = createBrowserHistory();

const Routing: React.FC = () => (<Router history={history}>
    <Switch>
        <Route path="/question/:id">
            <QuestionView/>
        </Route>
        <Route path="/">
            <Home/>
        </Route>
    </Switch>
</Router>);

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

    const items: IBreadcrumbItem[] = [
        {text: 'Files', key: 'Files'},
        {text: 'Folder 1', key: 'f1'},
    ];

    const user = useTypedSelector(userSelector);
    const Body = !!user ? <Routing/> : <Login/>;
    return (
        <div className="App" style={{padding: "20px"}}>
            {user ? <Breadcrumb items={items} style={{padding: "0px 0px 10px 0px"}}/> : null}
            {loaded ? Body : <PageSpinner/>}
        </div>
    );
};

export const App: React.FC = () => (
    <Provider store={store}>
        <AppView/>
    </Provider>
);
