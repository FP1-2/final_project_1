import {workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const addToFavourites = createAsyncThunk(
    'friends/addToFavourites',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/favorites/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);
