import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getGroup} from "./thunks";
import builders from "../builders";

const groupsReducer = createSlice({
    name: "groups",
    initialState: initialValue,
    reducers: {
        clearGroup: (state) => {
            state.getGroup = { ...initialValue.getGroup}
        },
    },
    extraReducers:(builder)=>{
        builders(builder, getGroup, 'getGroup');
    }
});

export const {
    clearGroup,
} = groupsReducer.actions;

export default groupsReducer.reducer;