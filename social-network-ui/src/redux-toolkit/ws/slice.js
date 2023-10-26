import { createSlice } from "@reduxjs/toolkit";
import initialState from "./initialState";


const webSocketReducer = createSlice({
    name: 'webSocket',
    initialState: initialState,
    reducers: {
        setNewMessage: (state, action) => {
            state.newMessage = action.payload;
        },
        setMessageWithNewStatus: (state, action) => {
            state.newMessage = action.payload;
        },
    }
});

export const{
    setNewMessage,
    setMessageWithNewStatus,
} = webSocketReducer.actions;

export default webSocketReducer.reducer;
