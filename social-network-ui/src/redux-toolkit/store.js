import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice"

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,

    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
});

export default store;