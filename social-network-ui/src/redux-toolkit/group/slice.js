import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {} from "./thunks";
import builders from "../builders";

const groupsReducer = createSlice({
    name: "groups",
    initialState: initialValue,
    reducers: {

    },
    extraReducers:(builder)=>{

    }
});

export const {

} = groupsReducer.actions;

export default groupsReducer.reducer;