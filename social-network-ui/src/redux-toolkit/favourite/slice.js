import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addToFavourites, favouritesList, isFavourite,deleteFavourite} from "./thunks";
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
        builders(builder, favouritesList,'favouritesList');
        builders(builder, isFavourite,'isFavourite');
        builders(builder, deleteFavourite,'deleteFavourite');
    }
});

export const {} = favouritesReducer.actions;

export default favouritesReducer.reducer;