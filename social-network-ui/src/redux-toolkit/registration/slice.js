import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { registrationThunkRequest,confirmRegistrationRequest } from "./thunks";
import builders from "../builders";

const registrationReducer = createSlice({
    name: "registration",
    initialState: initialValue,
    reducers: {

    },
    extraReducers:(builder)=>{
        builders(builder, registrationThunkRequest,'registrationMassage'),
        builders(builder, confirmRegistrationRequest,'confirmRegistrationMessage')
    }
})

export const {

} = registrationReducer.actions;

export default registrationReducer.reducer;