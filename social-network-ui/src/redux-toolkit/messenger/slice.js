import {createSlice} from "@reduxjs/toolkit";
import builders from "../builders";
import {
  createChat,
  loadChat,
  loadChats,
  loadMessages,
  loadUnreadMessagesQt,
  searchChat,
  searchUser
} from "./asyncThunk";
import defaultInitialState from "./defaultState";


const chatsReducer = createSlice({
  name: 'messenger',
  initialState: defaultInitialState,
  reducers: {
    updateChats: (state, action) => {
      const newMess = action.payload;

      const updatedChats = state.chats.obj.map(c => {
        if (newMess.chat.id === c.id) {
          return {...c, lastMessage: newMess};
        }
        return c;
      })
      const chatExists = updatedChats.some(c => c.id === newMess.chat.id);
      if (!chatExists) {
        updatedChats.push({
          id: newMess.chat.id,
          chatParticipant: newMess.chat.chatParticipant,
          lastMessage: newMess
        });
      }
      state.chats.obj = updatedChats;
    },
    updateChatsLastMessage: (state, action) => {
      const newMess = action.payload;
      state.chats.obj = state.chats.obj.map(c => {
        if (newMess.chat.id === c.id) {
          return {...c, lastMessage: {...c.lastMessage, status: "READ"}}
        }
        return c
      });
    },
    setUnreadMessagesQt: (state, action) => {
      state.unreadMessagesQt.obj = action.payload;
    },
    addChat: (state, action) => {
      const newChat = action.payload
      const chat = state.chats.obj.find(c => c.id === newChat.id)
      if (!chat) {
        state.chats.obj.unshift(action.payload);
      }
    },
    resetSearchUsers: (state, action) => {
      state.searchUsers = defaultInitialState.searchUsers;
    },
    resetSearchChats: (state, action) => {
      state.searchChats = defaultInitialState.searchChats;
    },
    resetChat: (state) => {
      state.chat = defaultInitialState.chat;
    },
    resetMessages: (state) => {
      state.messages = defaultInitialState.messages;
    },
  },
  extraReducers: (builder) => {
    builders(builder, loadChats, 'chats');
    builders(builder, loadChat, 'chat');
    builders(builder, loadMessages, 'messages');
    builders(builder, loadUnreadMessagesQt, 'unreadMessagesQt');
    builders(builder, createChat, 'chat');
    builders(builder, searchChat, 'searchChats');
    builders(builder, searchUser, 'searchUsers');
  }
});

export const {
  updateChats,
  updateChatsLastMessage,
  setUnreadMessagesQt,
  addChat,
  resetSearchUsers,
  resetSearchChats,
  resetChat,
  resetMessages
} = chatsReducer.actions;

export default chatsReducer.reducer;
