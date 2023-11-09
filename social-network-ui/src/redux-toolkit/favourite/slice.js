import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {addToFavourites, favouritesList, isFavourite,deleteFavourite} from "./thunks";
import builders, {buildersPagination} from '../builders';

const favouritesReducer = createSlice({
    name: "favourites",
    initialState: initialValue,
    reducers: {
        setIsFavourite: (state, action) => {
            state.isFavourite.obj = action.payload;
        },
        deleteLocalFavourite: (state, action) => {
            state.favouritesList.obj.content = state.favouritesList.obj.content.filter(
              favourite => favourite.postId!== action.payload);
        },
        appendFavourites: (state, action) => {
            state.favouritesList.obj.content = [
                ...state.favouritesList.obj.content,
                ...action.payload.content
            ];
            state.favouritesList.obj.pageable.pageNumber = action.payload.pageable.pageNumber;
            state.favouritesList.obj.totalPages = action.payload.totalPages;
            state.favouritesList.obj.totalElements = action.payload.totalElements;
        },
        resetFavouritesState: (state) => {
            state.favouritesList = {
                obj: {
                    content: [],
                    pageable: {
                        pageNumber: 0
                    },
                    totalPages: 0,
                    totalElements: 0,
                },
                status: '',
                error: '',
            };
        },
    },
    extraReducers:(builder)=>{
        builders(builder, addToFavourites,'addToFavourites');
        buildersPagination(builder, favouritesList, 'favouritesList');
        builders(builder, isFavourite,'isFavourite');
        builders(builder, deleteFavourite,'deleteFavourite');
    }
});

export const {setIsFavourite, resetFavouritesState, deleteLocalFavourite, appendFavourites} = favouritesReducer.actions;

export default favouritesReducer.reducer;