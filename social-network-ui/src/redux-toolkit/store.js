import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice";
import loginReducer from "./login/slice";

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,
        login: loginReducer,
    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
});

export default store;