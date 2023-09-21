import axios from "axios";
import { basicAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    return (await axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle)).data.url;

}

export const registrationThunkRequest = createAsyncThunk(
    "registration/data",
    async (obj,{rejectWithValue}) => {
        try{
            console.log(obj)
            const response =await basicAx.post("api/auth/signup", obj);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)