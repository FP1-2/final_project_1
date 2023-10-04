// import { createSlice } from "@reduxjs/toolkit";
// import {confirmRegistrationRequest, registrationThunkRequest} from "../registration/thunks";
// import {loadChats} from "./asyncThunk";
//
//
// const websocketSlice = createSlice({
//     name: 'websocket',
//     defaultInitialState,
//     reducers: {},
//     extraReducers: (builder) => {
//         builder
//             .addCase(connectWebSocket.fulfilled, (state) => {
//                 state.status = 'connected';
//             })
//             .addCase(receiveMessage, (state, action) => {
//                 // state.messages.push(action.payload);
//             });
//     },
// });
//
//
// export const connectWebSocket = createAsyncThunk('messages/connectWebSocket', async (chatId, thunkAPI) => {
//     const socket = new SockJS('http://localhost:8080/ws');
//     const stompClient = Stomp.over(socket);
//
//     stompClient.connect({}, () => {
//         stompClient.subscribe(`/user/queue/chat/${chatId}`, (message) => {
//             const messageData = JSON.parse(message.body);
//             dispatch(receiveMessage(messageData));
//         });
//     });
// });
//
// export const receiveMessage = (message) => ({
//     type: 'messages/receiveMessage',
//     payload: message,
// });
//
// export const { receiveMessage } = websocketSlice.actions;
// export default websocketSlice.reducer;
