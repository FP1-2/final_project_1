import {workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const addToFavourites = createAsyncThunk(
    'favourites/addToFavourites',
    async (id, { rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/favorites/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const favouritesList = createAsyncThunk(
    'favourites/favouritesList',
    async (id,{ rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/favorites?sort=id,desc`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const isFavourite = createAsyncThunk(
    'favourites/isFavourite',
    async (id,{ rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/favorites/${id}/exists`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const deleteFavourite = createAsyncThunk(
    'favourites/deleteFavourite',
    async (id,{ rejectWithValue }) => {
        try {
            const response = await workAx("delete",`api/favorites/${id}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

