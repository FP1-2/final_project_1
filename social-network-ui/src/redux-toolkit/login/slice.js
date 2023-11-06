import {createSlice} from '@reduxjs/toolkit';
import initialValue from './initialValue';
import builders from '../builders';
import { loadAuthToken, loadAuthUser } from './thunks';

const loginReducer = createSlice({
    name: 'auth',
    initialState: initialValue,
    reducers: {},
    extraReducers: (builder) => {
        builders(builder, loadAuthToken, 'token');
        builders(builder, loadAuthUser, 'user');
    }
});

export const {

} = loginReducer.actions;

export default loginReducer.reducer;
