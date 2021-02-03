import React from 'react';
import ReactDOM from 'react-dom';
import "office-ui-fabric-core/dist/css/fabric.css"
import {initializeIcons} from 'office-ui-fabric-react/lib/Icons';
import './index.css';
import {App} from './App';

initializeIcons();
ReactDOM.render(<App/>, document.getElementById('root'));
