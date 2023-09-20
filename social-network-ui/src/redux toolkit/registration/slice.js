import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { registrationThunkRequest, getTokenRequest } from "./thunks";
import builders from "../../builders";

const registrationReducer = createSlice({
    name: "registrationReducer",
    initialState:initialValue,
    reducers: {
        userRegistration: (state, action) => {
            state.registration.user = action.payload;
        },
        userRegistrationStatus: (state, action) => {
            state.registration.status = action.payload;
        },
        userGetToken: (state, action) => {
            state.registration.token = action.payload;
        }
    },
    extraReducers:(builder)=>{
        builders(builder,registrationThunkRequest,'registration',"user")
    }
})

export const {userRegistration,userGetToken,userRegistrationStatus} = registrationReducer.actions;
export default registrationReducer.reducer;