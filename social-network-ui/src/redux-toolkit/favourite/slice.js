import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addToFavourites, favouritesList, isFavourite,deleteFavourite} from "./thunks";
import builders from "../builders";

const favouritesReducer = createSlice({
    name: "favourites",
    initialState: initialValue,
    reducers: {
        setIsFavourite: (state, action) => {
            state.isFavourite.obj = action.payload;
        },
        // deleteLocalFavourite: (state, action) => {
        //     state.favouritesList.obj.content=state.favouritesList.obj.content.filter(el=>el.postId!==action.payload)
        // },
        deleteLocalFavourite: (state, action) => {
            state.favouritesList.obj.content = state.favouritesList.obj.content.filter(
              favourite => favourite.postId!== action.payload);
          },
    },
    extraReducers:(builder)=>{
        builders(builder, addToFavourites,'addToFavourites');
        builders(builder, favouritesList,'favouritesList');
        builders(builder, isFavourite,'isFavourite');
        builders(builder, deleteFavourite,'deleteFavourite');
    }
});

export const {setIsFavourite, deleteLocalFavourite} = favouritesReducer.actions;

export default favouritesReducer.reducer;