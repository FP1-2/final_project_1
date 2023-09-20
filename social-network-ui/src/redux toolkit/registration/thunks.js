import axios from "axios";
import { basicAx} from "../../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    const fileLink = (await axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle)).data.url;
    return fileLink;
}

export const registrationThunkRequest =createAsyncThunk(
    "registrationReducer/registrationThunkRequest",
    async (obj,{rejectWithValue}) => {
        try{
            await basicAx.post("api/auth/signup", obj);
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)

export const getTokenRequest = async (obj) => {
        try {
            const token =await basicAx.post('api/auth/token',obj)
            return token;
        } catch (error) {
            return console.log(error);
        }
    }