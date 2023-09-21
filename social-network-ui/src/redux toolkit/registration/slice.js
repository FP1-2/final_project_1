import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { registrationThunkRequest } from "./thunks";
import builders from "../builders";

const registrationReducer = createSlice({
    name: "registration",
    initialState: initialValue,
    reducers: {

    },
    extraReducers:(builder)=>{
        builders(builder, registrationThunkRequest,'registrationMassage')
    }
})

export const {

} = registrationReducer.actions;

export default registrationReducer.reducer;