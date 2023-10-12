import { createAsyncThunk } from '@reduxjs/toolkit';
import { basicAx } from '../ax.js';
/* import { setToken, setError, setLoading } from './slice';
 */
import { saveTokenToLocalStorage } from '../../utils/localStorageHelper';

export const loginThunk = createAsyncThunk(
  'auth/data',
  async (obj, { rejectWithValue }) => {
    try {
      const response = await basicAx.post('api/auth/token', obj);
      const responseToken = response.data;
      const { token } = responseToken;
      saveTokenToLocalStorage(token);
      return responseToken;
    } catch (err) {
      return rejectWithValue(err.response.data);
    }
  }
);
/* export const loginThunk = createAsyncThunk('auth/login', async (credentials, { dispatch }) => {
  dispatch(setLoading());
  try {
    const response = await fetch('http://13.53.82.220:9000/api/auth/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message);
    }

    const data = await response.json();
    dispatch(setToken(data.token));
  } catch (error) {
    dispatch(setError(error.message));
  }
});
 */
