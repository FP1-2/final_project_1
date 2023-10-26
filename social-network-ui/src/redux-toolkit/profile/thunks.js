import axios from "axios";
import { basicAx, workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    const response= await(axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle));
    return response;
}

export const getUserThunkRequest = createAsyncThunk(
    "profile/data",
    async ({token, id},{rejectWithValue}) => {
        try{
            const response = await basicAx.get(`api/users/${id}`, {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              });
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)

export const editUserThunkRequest = createAsyncThunk(
    "profile/dat",
    async (obj,{rejectWithValue}) => {
        try{
            // const response = await workAx("post",`api/users/edit`,obj)
            // console.log(response.data);
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)