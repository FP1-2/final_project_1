import { basicAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";


export const resetThunkRequest = createAsyncThunk(
  'resetPassword/data',
  async ({email}, { rejectWithValue}) => {
    try {
     const response = await basicAx.post('api/users/reset-password', email,{
          headers: {
              'Content-Type': 'text/plain'
          }
          
      });
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
)

