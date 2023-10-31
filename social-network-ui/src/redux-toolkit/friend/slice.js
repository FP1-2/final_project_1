import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getFriends,deleteFriend,requestToFriend,confirmFriendRequest} from "./thunks";
import builders from "../builders";

const friendReducer = createSlice({
    name: "friend",
    initialState: initialValue,
    reducers: {},
    extraReducers:(builder)=>{
        builders(builder, getFriends,'getFriends');
        builders(builder, deleteFriend,'deleteFriend');
        builders(builder, requestToFriend,'requestToFriend');
        builders(builder, confirmFriendRequest,'confirmFriendRequest');
    }
});

export const {} = friendReducer.actions;

export default friendReducer.reducer;