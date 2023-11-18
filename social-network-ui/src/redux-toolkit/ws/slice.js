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
    setNotificationQt: (state, action) => {
      state.notificationQt = action.payload;
    },
    resetNotificationQt: (state) => {
      state.notificationQt = initialState.notificationQt;
    },
  }
});

export const {
  setNewMessage,
  setMessageWithNewStatus,
  setIsVisible,
  setNotificationQt,
  resetNotificationQt
} = webSocketReducer.actions;

export default webSocketReducer.reducer;
