import { createAsyncThunk } from '@reduxjs/toolkit';
import { basicAx } from '../ax.js';
import {
  saveTokenToLocalStorage,
  getTokenFromLocalStorage,
  saveUserToLocalStorage
} from '../../utils/localStorageHelper';



export const loginThunk = createAsyncThunk(
  'auth/data',
  async (obj, { rejectWithValue }) => {
    try {
      const response = await basicAx.post('api/auth/token', obj);
      const responseToken = response.data;
      const { token, id } = responseToken;
      saveTokenToLocalStorage(token);

      const responseUser = await basicAx.get(`api/users/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const userInfo = responseUser.data;
      saveUserToLocalStorage(userInfo);

      return responseToken;
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);

