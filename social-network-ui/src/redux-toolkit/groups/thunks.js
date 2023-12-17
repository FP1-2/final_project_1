import {workAx} from "../ax";
import {createAsyncThunk} from "@reduxjs/toolkit";

export const getGroup = createAsyncThunk(
    'groups/getGroup',
    async ({id}, {rejectWithValue}) => {
        try {
            const response = await workAx("get", `api/groups/${id}`);
            const x = response.data;
            console.log("id in the createAsyncThunk: ", id);
            return x;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

