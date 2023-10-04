import { createSlice } from "@reduxjs/toolkit";
import builders from "../builders";
import {createChat, loadChat, loadChats} from "./asyncThunk";


export const chatsSlice = createSlice({
    name: 'chats',
    defaultInitialState,
    reducers: {
        receiveMessage: (state, action) => {
            state.push(action.payload);
        },
        setChats: (state, action)=>{
            state.chats = action.payload;
        },
        setChat: (state, action)=>{
            state.chat = action.payload;
        },
        setMessages: (state, action)=>{
            state.messages = action.payload;
        },
        setMessage: (state, action)=>{
            state.message = action.payload;
        },
    },
    extraReducers:(builder)=>{
        builders(builder, loadChats,'chats');
        builders(builder, loadChat,'chat');
        builders(builder, createChat,'chat');
        // TODO: deletechat
        // builders(builder, deleteChat,'chat');
    }
});

