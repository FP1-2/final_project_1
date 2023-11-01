import {configureStore,  combineReducers} from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice";
import loginReducer from "./login/slice";
import resetPasswordSlice from "./ResetPassword/slice"
import passwordUpdateReducer from "./UpdatePassword/slice"

const rootReducer = (state, action) => {
    return combineReducers({
        registration: registrationReducer,
        login: loginReducer,
        resetPassword: resetPasswordSlice,
        passwordUpdate: passwordUpdateReducer,

    })(state, action);
}

const store = configureStore({
    reducer: rootReducer,
});

export default store;

