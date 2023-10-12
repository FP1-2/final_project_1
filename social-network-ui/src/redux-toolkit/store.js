import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice";
import loginReducer from "./login/slice";
import registrationReducer from "./registration/slice"
import {chatsSlice} from "./messenger/slice";

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,
        login: loginReducer,
        chats: chatsSlice
    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
});

export default store;