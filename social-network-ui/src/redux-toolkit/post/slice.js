import {createSlice} from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {
    addPost,
    addRepost,
    getCommentsPost,
    addComment,
    addLike,
    editPost,
    deletePost,
    postsUser,
    getPost,
} from "./thunks";
import builders, {buildersPagination} from '../builders';
import {appendPaginationUtil} from "../../utils/utils";

const postReducer = createSlice({
    name: "post",
    initialState: initialValue,
    reducers: {
        clearComments: (state) => {
            state.getCommentsPost = {
                ...initialValue.getCommentsPost,
            };
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
            state.postObj = action.payload;
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
        appendComments: (state, action) => {
            appendPaginationUtil(state, action);
        },
        resetPostsState: (state) => {
            state.postsUser = {
                ...initialValue.postsUser,
            };
        },
        appendComment: (state, action) => {
            state.getCommentsPost.obj.content = [
                ...state.getCommentsPost.obj.content,
                action.payload
            ];
        },
        appendCommentStart: (state, action) => {
            state.getCommentsPost.obj.content = [
                action.payload,
                ...state.getCommentsPost.obj.content,
            ];
        },
        toggleLikePost: (state, action) => {
            const userId = action.payload;
            const post = state.getPost.obj;
            if (!post.likes) post.likes = [];
            const likes = post.likes;
            if (likes.includes(userId)) {
                state.getPost.obj.likes = likes.filter(id => id !== userId);
            } else {
                state.getPost.obj.likes = [...likes, userId];
            }
        },
        clearStatePost: (state) => {
            state.initialState = {...initialValue}
        },
        deleteLocalPost: (state, action) => {
            state.postsUser.obj.content = state.postsUser.obj.content.filter(
              post => post.postId!== action.payload);
        },
        appendPost: (state, action) => {
            state.postsUser.obj.content = [
                ...state.postsUser.obj.content,
                action.payload
            ];
        },
    },
    extraReducers: (builder) => {
        buildersPagination(builder, postsUser, 'postsUser');
        builders(builder, addPost, 'addPost');
        builders(builder, addRepost, 'addRepost');
        builders(builder, editPost, 'editPost');
        buildersPagination(builder, getCommentsPost, 'getCommentsPost');
        builders(builder, addComment, 'addComment');
        builders(builder, addLike, 'addLike');
        builders(builder, deletePost, 'deletePost');
        builders(builder, getPost, 'getPost');
    }
});

export const {
    clearComments,
    modalEditPostState,
    setPost,
    modalAddPostState,
    modalAddRepostState,
    appendPosts,
    resetPostsState,
    appendComments,
    appendComment,
    toggleLikePost,
    clearStatePost,
    deleteLocalPost,
    appendPost,
    appendCommentStart
} = postReducer.actions;

export default postReducer.reducer;