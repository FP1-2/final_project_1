import { workAx } from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getFriends = createAsyncThunk(
    'friends/getFriends',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/friends/list/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const deleteMyFriend = createAsyncThunk(
    'friends/deleteFriend',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("delete",`api/friends/delete`,obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const requestToFriend = createAsyncThunk(
    'friends/requestToFriend',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/friends/send-request`,obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const confirmFriendRequest = createAsyncThunk(
    'friends/confirmFriendRequest',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("put",`api/friends/update-status`,obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const friend = createAsyncThunk(
    'friends/friend',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/users/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const requestsToMe = createAsyncThunk(
    'friends/requestsToMe',
    async (text,{ rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/friends/list/friend-requests`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);