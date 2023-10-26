import { createSlice } from '@reduxjs/toolkit';
import initialValue from './initialValue';
import {loginThunk, userThunk} from './thunks';
import builders from '../builders';


const authSlice = createSlice({
  name: 'auth',
  initialState: initialValue,
  reducers:{
  },
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
