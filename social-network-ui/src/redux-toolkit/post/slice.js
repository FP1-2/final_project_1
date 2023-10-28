import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addPost,addRepost} from "./thunks";
import builders from "../builders";

const postReducer = createSlice({
    name: "post",
    initialState: initialValue,
    reducers: {},
    extraReducers:(builder)=>{
        builders(builder, addPost,'addPost');
        builders(builder, addRepost,'addRepost');
    }
});

export const {} = postReducer.actions;

export default postReducer.reducer;