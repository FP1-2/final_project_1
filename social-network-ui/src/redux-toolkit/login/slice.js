import { createSlice } from '@reduxjs/toolkit';
import initialValue from './initialValue';
import {loginThunk} from './thunks';
import builders from '../builders';
/* const authSlice = createSlice({
  name: 'auth',
  initialState: initialValue,
  reducers: {
    setToken: (state, action) => {
      state.token = action.payload;
      state.error = null;
      state.loading = false;
    },
    setError: (state, action) => {
      state.error = action.payload;
      state.loading = false;
    },
    setLoading: (state) => {
      state.loading = true;
    },
  },
}); */

const authSlice = createSlice({
  name: 'auth',
  initialState: initialValue,
  reducers:{},
  extraReducers:(builder)=>{
    builders(builder, loginThunk, 'login');
}
});
/* export const { setToken, setError, setLoading } = authSlice.actions;
 */
/* export const selectToken = (state) => state.auth.token;
export const selectError = (state) => state.auth.error;
export const selectLoading = (state) => state.auth.loading;
 */
export default authSlice.reducer;
