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
    },
    extraReducers: (builder) => {
        buildersPagination(builder, loadPostsInMain, 'posts');
    }
});

export const {
    appendPostsInMain,
    resetPostsInMainState,
} = PostsInMainReducer.actions;

export default PostsInMainReducer.reducer;
