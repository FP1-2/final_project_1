import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addToFavourites} from "./thunks";
import builders from "../builders";

const favouritesReducer = createSlice({
    name: "favourites",
    initialState: initialValue,
    reducers: {
        // modalDeleteFriendState: (state, action) => {
        //     state.modalDeleteFriend = action.payload;
        // },
    },
    extraReducers:(builder)=>{
        builders(builder, addToFavourites,'addToFavourites');
    }
});

export const {} = favouritesReducer.actions;

export default favouritesReducer.reducer;