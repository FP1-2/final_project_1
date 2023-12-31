import { createAsyncThunk } from '@reduxjs/toolkit';
import { workAx } from '../ax.js';
import {appendPostsInMain} from "./slice";

export const loadPostsInMain = createAsyncThunk(
    'postsInMain/posts',
    async ({ page = 0, size = 10 }, { dispatch, rejectWithValue }) => {
        const params = new URLSearchParams({ page, size });
        try {
            const response = await workAx('get', `/api/posts?${params}`);
            if (page > 0) {
                dispatch(appendPostsInMain({
                    key: "posts",
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