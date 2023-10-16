import { createSlice } from '@reduxjs/toolkit';
import initialValue from './initialValue';
import {loginThunk} from './thunks';
import builders from '../builders';


const authSlice = createSlice({
  name: 'auth',
  initialState: initialValue,
  reducers:{},
  extraReducers:(builder)=>{
    builders(builder, loginThunk, 'login');
}
});

export default authSlice.reducer;
