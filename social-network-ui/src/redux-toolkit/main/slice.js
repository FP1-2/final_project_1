import {createSlice} from '@reduxjs/toolkit';
import {buildersPagination} from '../builders';
import {loadPostsInMain} from './thunks';
import defaultInitialState from "./defaultState";
import {appendPaginationUtil} from "../../utils/utils";

const PostsInMainReducer = createSlice({
    name: 'postsInMain',
    initialState: defaultInitialState,
    reducers: {
        appendPostsInMain: (state, action) => {
            appendPaginationUtil(state, action)
        },
        resetPostsInMainState: (state) => {
            state.posts = defaultInitialState.posts;
        },
        deletLocalMainPost: (state, action) => {
            state.posts.obj.content = state.posts.obj.content.filter(
                post => post.postId !== action.payload);
        },
        addNewPost: (state, action) => {
            state.posts.obj.content.unshift(action.payload);
        },
    },
    extraReducers: (builder) => {
        buildersPagination(builder, loadPostsInMain, 'posts');
    }
});

export const {
    appendPostsInMain,
    resetPostsInMainState,
    deletLocalMainPost,
    addNewPost
} = PostsInMainReducer.actions;

export default PostsInMainReducer.reducer;
