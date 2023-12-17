import {workAx} from "../ax";
import {createAsyncThunk} from "@reduxjs/toolkit";
import {addUserGroups} from "./slice";

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
export const getUserGroups = createAsyncThunk(
  'groups/userGroups',
  async ({page = 0, size= 10, id}, {dispatch, rejectWithValue}) => {
    const params = new URLSearchParams({ page, size });
    try {
      const response = await workAx("get", `api/groups/user/${id}?${params}`);
      if (page > 0) {
        dispatch(addUserGroups(response.data));
      } else {
        return response.data;
      }
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);

