import {createAsyncThunk} from "@reduxjs/toolkit";
import {basicAx} from "../ax";

export const loadChats = createAsyncThunk(
    "messenger/chats",
    async ({page, size},{rejectWithValue}) => {
        try{
            const response = await basicAx.get("api/chats?page=${page}&size=${size}");
            if(!response.ok){
                throw new Error("Error in chats loading.");
            }
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
export const loadChat = createAsyncThunk(
    "messenger/chat",
    async (id,{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/chats/${id}`);
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
export const createChat = createAsyncThunk(
    "messenger/chat",
    async (username,{rejectWithValue}) => {
        try{
            const response = await basicAx.post(`api/chats/${username}`);
            if(!response.ok){
                throw new Error("Error in creating new chat.");
            }
            return response.data;
        }
        catch(err){
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
