import React, {useEffect, useState} from "react";
import {Provider, useDispatch} from "react-redux";
import {Route, Router, Switch, useLocation} from "react-router";
import {createBrowserHistory, Location} from "history";
import axios from "axios";
import {Breadcrumb, IBreadcrumbItem} from "office-ui-fabric-react";
import {store, useTypedSelector} from "./state/Store";
import {SocketStatus, socketStatusSelector} from "./state/SocketModule";
import {Question} from "./state/ServerRPC";
import {PageSpinner} from "./components/PageSpinner";
import {loginSuccessfulAction, userSelector} from "./routes/Login/LoginModule";
import {Login} from "./routes/Login/Login";
import {Home} from "./routes/Home/Home";
import {Admin} from "./routes/Admin/Admin.js";
import {questionSelector} from "./routes/Question/QuestionModule";
import {QuestionView} from "./routes/Question/QuestionView";
import "./App.css";

const history = createBrowserHistory();

const Routing: React.FC = () => (
    <Switch>
        <Route path="/secretAdminRoute">
            <Admin/>
        </Route>
        <Route path="/question/:id">
            <QuestionView/>
        </Route>
        <Route path="/">
            <Home/>
        </Route>
    </Switch>
);

const createBreadCrumb = (location: Location, question: Question | undefined): IBreadcrumbItem[] => {
    const crumb: IBreadcrumbItem[] = [
        {
            text: 'Home', key: 'Home', onClick: () => {
                history.push('/')
            }
        },
    ];
    const paths = location.pathname.trim().split('/');
    if (paths[1] === 'question' && !!question) {
        crumb.push({text: question?.title, key: question?.title});
    }
    return crumb;
};

const AppView: React.FC = () => {
    const [loaded, setLoaded] = useState(false);
    const dispatch = useDispatch();

    const location = useLocation();
    const question = useTypedSelector(questionSelector);
    const items = createBreadCrumb(location, question);

    const connected = useTypedSelector(socketStatusSelector) === SocketStatus.Connected;
    const user = useTypedSelector(userSelector);

    useEffect(() => {
        axios("/api/user").then(response => {
            if (!!response.data) {
                dispatch(loginSuccessfulAction(response.data, response.data))
            }
            setLoaded(true);
        });
    }, [dispatch]);

    const Body = !!user ? <Routing/> : <Login/>;
    return (
        <div className="App" style={{padding: "20px"}}>
            {user ? <Breadcrumb items={items} style={{padding: "0px 0px 10px 0px"}}/> : null}
            {!loaded || (!!user && !connected) ? <PageSpinner/> : Body}
        </div>
    );
};

export const App: React.FC = () => (
    <Provider store={store}>
        <Router history={history}>
            <AppView/>
        </Router>
    </Provider>
);
