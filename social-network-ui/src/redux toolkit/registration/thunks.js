import axios from "axios";
import { basicAx} from "../ax";
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
           const response=await basicAx.post("api/auth/signup", obj);
           return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)
