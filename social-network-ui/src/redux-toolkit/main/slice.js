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
        setRecentSearch:(state, action) => {
            const payload = action.payload;
            const existingIndex = state.recentSearch.findIndex(item => item.id === payload.id);
            if(existingIndex !== -1) {
                state.recentSearch.splice(existingIndex, 1);
            }
            state.recentSearch = [payload, ...state.recentSearch.slice(0, 9)]
        },
        deleteFromRecentSearch: (state, action) => {
            state.recentSearch = state.recentSearch.filter(item => item.id !== action.payload);
        }
    },
    extraReducers: (builder) => {
        buildersPagination(builder, loadPostsInMain, 'posts');
    }
});

export const {
    appendPostsInMain,
    resetPostsInMainState,
    deletLocalMainPost,
    addNewPost,
    setRecentSearch,
    deleteFromRecentSearch
} = PostsInMainReducer.actions;

export default PostsInMainReducer.reducer;
