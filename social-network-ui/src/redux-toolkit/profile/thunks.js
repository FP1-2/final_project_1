import axios from "axios";
import {workAx} from "../ax";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    const response= await(axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle));
    return response;
}

export const loadUserProfile = createAsyncThunk(
    'profile/profileUser',
    async ({id,user}, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/users/${id}`);
            return {...response.data, user:user};
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const editUser = createAsyncThunk(
    "profile/editUser",
    async (obj,{rejectWithValue}) => {
        try{
            const response = await workAx("put",`api/users/edit`,obj);
            return response.data;
        }
        catch(err){
            return rejectWithValue (err.response.data);
        }
    }
)

