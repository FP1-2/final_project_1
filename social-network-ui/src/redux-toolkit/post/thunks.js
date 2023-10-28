import axios from "axios";
import { basicAx, workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const addPost = createAsyncThunk(
    'post/addPost',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/posts/post`,obj);
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
            const response = await workAx("post",`api/posts/repost`,obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);
