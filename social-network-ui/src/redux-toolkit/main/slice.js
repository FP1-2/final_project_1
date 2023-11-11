import {createSlice} from '@reduxjs/toolkit';
import {buildersPagination} from '../builders';
import {loadPostsInMain} from './thunks';
import defaultInitialState from "./defaultState";

const PostsInMainReducer = createSlice({
    name: 'postsInMain',
    initialState: defaultInitialState,
    reducers: {
        appendPostsInMain: (state, action) => {
            state.posts.obj.content = [
                ...state.posts.obj.content,
                ...action.payload.content
            ];
            state.posts.obj.pageable.pageNumber = action.payload.pageable.pageNumber;
            state.posts.obj.totalPages = action.payload.totalPages;
            state.posts.obj.totalElements = action.payload.totalElements;
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
