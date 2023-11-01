import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getFriends,deleteFriend,requestToFriend,confirmFriendRequest,friend, requestsToMe} from "./thunks";
import builders from "../builders";

const friendsReducer = createSlice({
    name: "friends",
    initialState: initialValue,
    reducers: {
        modalDeleteFriendState: (state, action) => {
            state.modalDeleteFriend = action.payload;
        },
    },
    extraReducers:(builder)=>{
        builders(builder, getFriends,'getFriends');
        builders(builder, deleteFriend,'deleteFriend');
        builders(builder, requestToFriend,'requestToFriend');
        builders(builder, confirmFriendRequest,'confirmFriendRequest');
        builders(builder, friend, "friend");
        builders(builder, requestsToMe, "requestsToMe");
    }
});

export const {modalDeleteFriendState} = friendsReducer.actions;

export default friendsReducer.reducer;