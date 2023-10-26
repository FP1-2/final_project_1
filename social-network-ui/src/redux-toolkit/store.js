import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice";
import loginReducer from "./login/slice";
import chatsReducer from "./messenger/slice";
import webSocketReducer from "./ws/slice"
import webSocketMiddleware from "./ws/webSocketMiddleware"

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,
        login: loginReducer,
        messenger: chatsReducer,
        webSocket: webSocketReducer
    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(webSocketMiddleware),
});

export default store;