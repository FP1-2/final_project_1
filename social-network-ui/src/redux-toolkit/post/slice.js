import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getPost,addPost,addRepost,getCommentsPost,addComment,addLike, editPost} from "./thunks";
import builders from "../builders";

const postReducer = createSlice({
    name: "post",
    initialState: initialValue,
    reducers: {
        clearComments: (state) => {
            state.getCommentsPost.obj = {};
        },
        modalEditPostState: (state, action) => {
            state.modalEditPost = action.payload;
        },
        modalAddRepostState: (state, action) => {
            state.modalAddRepost = action.payload;
        },
        modalAddPostState: (state, action) => {
            state.modalAddPost = action.payload;
        },
        setPost: (state, action) => {
            state.postObj= action.payload;
        },
    },
    extraReducers:(builder)=>{
        builders(builder, addPost,'addPost');
        builders(builder, addRepost,'addRepost');
        builders(builder, editPost,'editPost');
        builders(builder, getCommentsPost,'getCommentsPost');
        builders(builder, addComment,'addComment');
        builders(builder, addLike,'addLike');
        // builders(builder, getPost,'getPost');
    }
});

export const {clearComments, modalEditPostState, setPost, modalAddPostState, modalAddRepostState} = postReducer.actions;

export default postReducer.reducer;