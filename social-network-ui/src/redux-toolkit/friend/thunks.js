import axios from "axios";
import { basicAx, workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getFriends = createAsyncThunk(
    'friend/getFriends',
    async ({ rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/friends/list`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);
export const deleteFriend = createAsyncThunk(
    'friend/deleteFriend',
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
    'friend/requestToFriend',
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
    'friend/confirmFriendRequest',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("put",`api/friends/update-status`,obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);