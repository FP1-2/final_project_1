import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice"
import {chatsSlice} from "./messenger/slice";

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,
        chats: chatsSlice
    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
});

export default store;