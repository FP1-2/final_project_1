import {createAsyncThunk} from "@reduxjs/toolkit";
import {basicAx} from "../ax";
const token = localStorage.getItem('authToken');

const config = {
    headers: {
    'Authorization': `Bearer ${token}`,
}}
export const loadChats = createAsyncThunk(
    "messenger/chats",
    async ({page, size},{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/chats?page=${page}&size=${size}`, config);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const loadChat = createAsyncThunk(
    "messenger/chat",
    async ({id},{rejectWithValue}) => {
        try{
            console.log("Sending chat request...");
            const response = await basicAx.get(`api/chats/${id}`, config);

            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const createChat = createAsyncThunk(
    "messenger/newChat",
    async ({username},{rejectWithValue}) => {
        try{
            const response = await basicAx.post(`api/chats/${username}`,null, config);
            
            return response.data;
        }
        catch(err){
            console.log(err)
            return rejectWithValue (err.response.data);
        }
    }
)
export const deleteChat = createAsyncThunk(
    "messenger/chat",
    async (chatId,{rejectWithValue}) => {
        try{
            const response = await basicAx.delete("api/chats/{chatId}");
            if(!response.ok){
                throw new Error("Error in chat loading.");
            }
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const loadMessages = createAsyncThunk(
    "messenger/messages",
    async ({id, page, size},{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/chats/messages/${id}?page=${page}&size=${size}`, config);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const loadUser = createAsyncThunk(
    'messenger/user',
    async ({id}, { rejectWithValue }) => {
      try {
        const response = await basicAx.get(`api/users/${id}`, config);
        return response.data;
      } catch (err) {
        return rejectWithValue(err.response.data);
      }
    }
  );
  export const loadUnreadMessagesQt = createAsyncThunk(
    "messenger/unread",
    async (_,{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/chats/messages/unread`, config);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const searchChat = createAsyncThunk(
    "messenger/searchChats",
    async ({input, page, size},{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/chats/search?input=${input}&page=${page}&size=${size}`, config);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const searchUser = createAsyncThunk(
    "messenger/searchUsers",
    async ({input, page, size},{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/users/search?input=${input}&page=${page}&size=${size}`, config);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)