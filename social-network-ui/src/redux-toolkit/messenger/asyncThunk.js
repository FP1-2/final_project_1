import {createAsyncThunk} from "@reduxjs/toolkit";
import {workAx} from "../ax";

export const loadChats = createAsyncThunk(
  "messenger/chats",
  async ({page, size}, {rejectWithValue}) => {
    try {
      const response = await workAx("get", `api/chats?page=${page}&size=${size}`);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)
export const loadChat = createAsyncThunk(
  "messenger/chat",
  async ({id}, {rejectWithValue}) => {
    try {
      const response = await workAx("get", `api/chats/${id}`);
      console.log("sending chat:"+id)
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)
export const createChat = createAsyncThunk(
  "messenger/newChat",
  async ({username}, {rejectWithValue}) => {
    try {
      const response = await workAx("post", `api/chats/${username}`, null);

      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)

export const loadMessages = createAsyncThunk(
  "messenger/messages",
  async ({id, page, size}, {rejectWithValue}) => {
    try {
      const response = await workAx('get', `api/chats/messages/${id}?page=${page}&size=${size}`);
    
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)
export const loadUnreadMessagesQt = createAsyncThunk(
  "messenger/unread",
  async (_, {rejectWithValue}) => {
    try {
      const response = await workAx('get', `api/chats/messages/unread`);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)
export const searchChat = createAsyncThunk(
  "messenger/searchChats",
  async ({input, page, size}, {rejectWithValue}) => {
    try {
      const response = await workAx('get', `api/chats/search?input=${input}&page=${page}&size=${size}`);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)
export const searchUser = createAsyncThunk(
  "messenger/searchUsers",
  async ({input, page, size}, {rejectWithValue}) => {
    try {
      const response = await workAx('get', `api/users/search?input=${input}&page=${page}&size=${size}`);
      return response.data;
    } catch (err) {
      return rejectWithValue(err.response);
    }
  }
)