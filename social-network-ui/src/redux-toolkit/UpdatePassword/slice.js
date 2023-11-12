import { createSlice } from '@reduxjs/toolkit';
import initialValue from './initialValue';
import { updatePasswordThunk } from './thunks';
import builders from '../builders';

const passwordUpdateSlice = createSlice({
  name: 'passwordUpdate',
  initialState: initialValue,
  reducers:{},
  extraReducers:(builder)=>{
    builders(builder, updatePasswordThunk, 'passwordUpdate');
}
});

export default passwordUpdateSlice.reducer;


