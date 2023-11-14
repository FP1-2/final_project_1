import { workAx } from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";
import {appendPosts} from "./slice";
import {appendComments} from "./slice";

export const getPost = createAsyncThunk(
    'post/getPost',
    async ({ id }, { rejectWithValue }) => {
        try {
            const response = await workAx("get", `api/posts/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const addPost = createAsyncThunk(
    'post/addPost',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("post", `api/posts/post`, obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const addRepost = createAsyncThunk(
    'post/addRepost',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("post", `api/posts/repost`, obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const editPost = createAsyncThunk(
    'post/editPost',
    async ({ obj, id }, { rejectWithValue }) => {
        try {
            const response = await workAx("patch", `api/posts/update/${id}`, obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const getCommentsPost = createAsyncThunk(
    'post/getCommentsPost',
    async ({ page = 0, size = 10, id }, { dispatch,rejectWithValue }) => {
        const params = new URLSearchParams({ page, size });
        try {
            const response = await workAx("get", `api/posts/${id}/comments?${params}`);
            if (page > 0) {
                dispatch(appendComments({
                    key: "getCommentsPost",
                    data: response.data
                }));
            } else {
                return response.data;
            }
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const addComment = createAsyncThunk(
    'post/addComment',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("post", `api/posts/comment`, obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const addLike = createAsyncThunk(
    'post/addLike',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("post", `api/posts/like/${id}`,);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const deletePost = createAsyncThunk(
    'post/deletePost',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("delete", `api/posts/delete/${id}`,);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const postsUser = createAsyncThunk(
    "post/postsUser",
    async ({ page = 0, size = 10, id }, { dispatch, rejectWithValue }) => {
        const params = new URLSearchParams({ page, size });
        try {
            const response = await workAx("get", `api/posts/by_user_id/${id}?${params}`);
            if (page > 0) {
                dispatch(appendPosts(response.data));
            } else {
                return response.data;
            }
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
)