import { createAsyncThunk } from '@reduxjs/toolkit';
import {basicAx, workAx} from '../ax.js';

export const loadAuthToken = createAsyncThunk(
    'auth/token',
    async (obj, { rejectWithValue }) => {
        try {
            const response = await basicAx.post('api/auth/token', obj);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const loadAuthUser = createAsyncThunk(
    'auth/user',
    async (userId, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/users/${userId}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);