import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {BrowserRouter} from 'react-router-dom';
import {Provider} from 'react-redux';
import {PersistGate} from "redux-persist/integration/react";
import {persist} from "./redux-toolkit/store";
import store from "./redux-toolkit/store"
import ".//styles/style.scss"

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <Provider store={store}>
        <BrowserRouter>
            <PersistGate loading={null} persistor={persist}>
                {/*<React.StrictMode>*/}
                    <App/>
                {/*</React.StrictMode>*/}
            </PersistGate>
        </BrowserRouter>
    </Provider>
);