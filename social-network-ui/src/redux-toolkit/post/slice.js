import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addPost,addRepost,getCommentsPost,addComment,addLike, editPost, deletePost, postsUser} from "./thunks";
import builders, {buildersPagination} from '../builders';

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
        appendPosts: (state, action) => {
            state.postsUser.obj.content = [
                ...state.postsUser.obj.content,
                ...action.payload.content
            ];
            state.postsUser.obj.pageable.pageNumber = action.payload.pageable.pageNumber;
            state.postsUser.obj.totalPages = action.payload.totalPages;
            state.postsUser.obj.totalElements = action.payload.totalElements;
        },
        resetPostsState: (state) => {
            state.postsUser = {
                obj: {
                    content: [],
                    pageable: {
                        pageNumber: 0
                    },
                    totalPages: 0,
                    totalElements: 0,
                },
                status: '',
                error: '',
            };
        },
    },
    extraReducers:(builder)=>{
        buildersPagination(builder, postsUser, 'postsUser');
        builders(builder, addPost,'addPost');
        builders(builder, addRepost,'addRepost');
        builders(builder, editPost,'editPost');
        builders(builder, getCommentsPost,'getCommentsPost');
        builders(builder, addComment,'addComment');
        builders(builder, addLike,'addLike');
        builders(builder, deletePost,'deletePost');
    }
});

export const {clearComments, modalEditPostState, setPost, modalAddPostState, modalAddRepostState, appendPosts, resetPostsState} = postReducer.actions;

export default postReducer.reducer;