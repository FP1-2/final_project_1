import { basicAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";


export const updatePasswordThunk = createAsyncThunk(
  'passwordUpdate/data',  
async ({ newPassword, token, email }, { rejectWithValue }) => {
    try {
      const response = await basicAx.put(`api/users/update-password/${token}`, {
        newPassword : newPassword,
        email: email,
      });
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);