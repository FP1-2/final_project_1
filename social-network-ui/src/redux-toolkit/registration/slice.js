import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { registrationThunkRequest,confirmRegistrationRequest } from "./thunks";
import builders from "../builders";

const registrationReducer = createSlice({
    name: "registration",
    initialState: initialValue,
    reducers: {
        resetRegistrationThunkRequest: (state) => {
            state.registrationMassage = initialValue.registrationMassage;
          },
    },
    extraReducers:(builder)=>{
        builders(builder, registrationThunkRequest,'registrationMassage');
        builders(builder, confirmRegistrationRequest,'confirmRegistrationMessage');
    }
});

export const {
    resetRegistrationThunkRequest
} = registrationReducer.actions;

export default registrationReducer.reducer;