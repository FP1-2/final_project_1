import axios from "axios";
import { basicAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    const response= await(axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle));
    return response;
}

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