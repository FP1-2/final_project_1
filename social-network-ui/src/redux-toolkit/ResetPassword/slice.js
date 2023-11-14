import { createSlice } from '@reduxjs/toolkit';
import initialValue from './initialValue';
import { resetThunkRequest } from './thunks';
import builders from '../builders';

const resetPasswordSlice = createSlice({
  name: 'resetPassword',
  initialState: initialValue,
  reducers:{},
  extraReducers:(builder)=>{
    builders(builder, resetThunkRequest, 'resetPassword');
}
});


export default resetPasswordSlice.reducer;


