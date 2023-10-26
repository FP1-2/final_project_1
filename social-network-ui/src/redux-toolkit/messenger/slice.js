import { createSlice } from "@reduxjs/toolkit";
import builders from "../builders";
import {createChat, loadChat, loadChats, loadMessages, loadUser, loadUnreadMessagesQt, searchChat, searchUser} from "./asyncThunk";
import defaultInitialState from "./defaultState";


const chatsReducer = createSlice({
    name: 'messenger',
    initialState: defaultInitialState,
    reducers: {
        setChats: (state, action)=>{
            state.chats = action.payload;
        },
        updateChats: (state, action)=>{
            const newMess = action.payload;
            
            const updatedChats = state.chats.obj.map(c => {
                if(newMess.chat.id === c.id) {
                    return {...c, lastMessage: newMess};
                } 
                return c;
            })
            const chatExists = updatedChats.some(c => c.id === newMess.chat.id);
            if (!chatExists) {
                updatedChats.push({
                    id: newMess.chat.id,
                    chatParticapant: newMess.chat.chatParticapant,
                    lastMessage: newMess
                });
            }
            state.chats.obj = updatedChats;
        },
        updateChatsLastMessage: (state, action)=>{
            const newMess = action.payload;
            const updatedChats =  state.chats.obj.map(c=>{
                if(newMess.chat.id === c.id) {
                    return {...c, lastMessage: {...c.lastMessage, status: "READ"}}
                }
                return c
            })
    
            state.chats.obj = updatedChats;
        },
        setChat: (state, action)=>{
            state.chat = action.payload;
        },
        setMessages: (state, action)=>{
            state.messagesList = action.payload;
        },
        setMessagesList: (state, action)=>{
            state.messagesList = [...state.messagesList, ...action.payload];
        },
        addMessage: (state, action) => {
            state.messagesList.unshift(action.payload);
        },
        updateMessages: (state, action)=>{
            const newStatus = action.payload
            const updatedMess =  state.messagesList.map(m=>{
                if(newStatus.id === m.id) {
                    return {...m, status: "READ"}
                }
                return m
            })
            state.messages.List = updatedMess;
        },
        setMessage:(state, action) =>{
            state.message = action.payload;
        },
        clearState: (state) => {
            return defaultInitialState;
          },
        setUnreadMessagesQt: (state, action) =>{
            state.unreadMessagesQt.obj = action.payload;
        },
        addChat: (state, action)=>{
            const newChat = action.payload
            const chat =  state.chats.obj.find(c => c.id === newChat.id)
            if(!chat){ state.chats.obj.unshift(action.payload);}
        }, 
        resetMessagesList: (state) => {
            return defaultInitialState.messagesList;
          },
        resetSearchUsers:  (state, action)=>{
            state.searchUsers = defaultInitialState.searchUsers;
        }, 
        resetSearchChats:  (state, action)=>{
            state.searchChats = defaultInitialState.searchChats;
        }, 
    },
    extraReducers:(builder)=>{
        builders(builder, loadChats,'chats');
        builders(builder, loadChat,'chat');
        builders(builder, loadMessages,'messages');
        builders(builder, loadUser,'user');
        builders(builder, loadUnreadMessagesQt,'unreadMessagesQt');
        builders(builder, createChat,'chat');
        builders(builder, searchChat,'searchChats');
        builders(builder, searchUser, 'searchUsers');
        // TODO: deletechat
        // builders(builder, deleteChat,'chat');
    }
});

export const{
    setChats,
    setMessages,
    addMessage,
    setMessage,
    updateChats,
    updateMessages,
    clearStat,
    updateChatsLastMessage,
    setUnreadMessagesQt,
    addChat,
    setMessagesList,
    setChat,
    resetMessagesList,
    resetSearchUsers,
    resetSearchChats
} = chatsReducer.actions;

export default chatsReducer.reducer;
