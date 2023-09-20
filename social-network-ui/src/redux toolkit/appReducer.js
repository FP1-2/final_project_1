import { combineReducers } from "@reduxjs/toolkit";
import registrationReducer from "./registration/slice";

const appReducer = combineReducers({
    registration: registrationReducer,
});
export default appReducer;