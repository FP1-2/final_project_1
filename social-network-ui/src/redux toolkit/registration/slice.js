import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { registrationThunkRequest, getTokenRequest } from "./thunks";
import builders from "../builders";

const registrationReducer = createSlice({
    name: "registrationReducer",
    initialState:initialValue,
    reducers: {},
    extraReducers:(builder)=>{
        builders(builder,registrationThunkRequest,'registration')
    }
})

export const {userRegistration,userGetToken,userRegistrationStatus} = registrationReducer.actions;
export default registrationReducer.reducer;