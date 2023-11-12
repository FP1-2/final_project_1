import { basicAx } from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const registrationThunkRequest = createAsyncThunk(
    "registration/data",
    async (obj,{rejectWithValue}) => {
        try{
            const response =await basicAx.post("api/auth/signup", obj);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)

export const confirmRegistrationRequest = createAsyncThunk(
    "confirmRegistrationMessage/data",
    async (url,{rejectWithValue}) => {
        try{
            const response =await basicAx.post(`api/auth/confirm${url}`);
            return response.data
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)