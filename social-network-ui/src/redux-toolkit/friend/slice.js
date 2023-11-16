import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getFriends,deleteMyFriend,requestToFriend,confirmFriendRequest,friend, requestsToMe, cancelRequest,allRequests} from "./thunks";
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
        builders(builder, deleteMyFriend,'deleteMyFriend');
        builders(builder, requestToFriend,'requestToFriend');
        builders(builder, confirmFriendRequest,'confirmFriendRequest');
        builders(builder, friend, "friend");
        builders(builder, requestsToMe, "requestsToMe");
        builders(builder, cancelRequest, "cancelRequest");
        builders(builder, allRequests, "allRequests");
    }
});

export const {modalDeleteFriendState, clearRequestToFriend} = friendsReducer.actions;

export default friendsReducer.reducer;