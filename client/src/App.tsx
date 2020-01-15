import React from 'react';
import './App.css';
import {Route, Router, Switch} from "react-router";
import {createBrowserHistory} from 'history';
import {Home} from "./routes/Home/Home";
import {Question} from "./routes/Question";
import {Provider} from "react-redux";
import {Login} from "./routes/Login/Login";
import {userSelector} from "./routes/Login/LoginModule";
import {store, useTypedSelector} from "./state/Store";

const history = createBrowserHistory();

const Routing: React.FC = () => <Router history={history}>
    <Switch>
        <Route path="/question/:id">
            <Question/>
        </Route>
        <Route path="/">
            <Home/>
        </Route>
    </Switch>
</Router>;

const AppView: React.FC = () => {
    const user = useTypedSelector(userSelector);
    return (
        <div className="App">
            {!!user
                ? <Routing/>
                : <Login/>
            }
            {user ? <h1>{user}</h1> : null}
        </div>
    );
};

export const App: React.FC = () => (
    <Provider store={store}>
        <AppView/>
    </Provider>
);
