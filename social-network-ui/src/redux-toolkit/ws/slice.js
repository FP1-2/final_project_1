import {createSlice} from "@reduxjs/toolkit";
import initialState from "./initialState";


const webSocketReducer = createSlice({
  name: 'webSocket',
  initialState: initialState,
  reducers: {
    setNewMessage: (state, action) => {
      state.newMessage = action.payload;
    },
    setMessageWithNewStatus: (state, action) => {
      state.messageWithNewStatus = action.payload;
    },
    setIsVisible: (state, action) => {
      state.isVisible = action.payload;
    },
    setNotification: (state, action) => {
      state.isVisible = action.payload;
    },
  }
});

export const {
  setNewMessage,
  setMessageWithNewStatus,
  setIsVisible,
  setNotification
} = webSocketReducer.actions;

export default webSocketReducer.reducer;
