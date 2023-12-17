import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getFriends,deleteMyFriend,requestToFriend,confirmFriendRequest,friend, requestsToMe, cancelRequest,allRequests,getMyFriends} from "./thunks";
import builders from "../builders";
import { friendSearchRequest } from "./thunks";

const friendsReducer = createSlice({
    name: "friends",
    initialState: initialValue,
    reducers: {
        modalDeleteFriendState: (state, action) => {
            state.modalDeleteFriend = action.payload;
        },
        clearFriends:(state) => {
            state.getFriends = {
                ...initialValue.getFriends,
            };
        },
        clearMyFriends:(state) => {
            state.getMyFriends = {
                ...initialValue.getMyFriends,
            };
        },
        cancelLocalRequest:(state, action) => {
            state.allRequests.obj.send = state.allRequests.obj.send.filter(
              user => user.id!== action.payload);
        },
        addLocalFriend:(state, action) => {
            state.getFriends.obj = [
                ...state.getFriends.obj,
                action.payload
            ];
        },
        deleteLocalReceived:(state, action) => {
            state.allRequests.obj.received = state.allRequests.obj.received.filter(user => user.id!== action.payload);
        },
        deleteLocalFriend:(state, action) => {
            state.getMyFriends.obj = state.getMyFriends.obj.filter(
              user => user.id!== action.payload);
        },
        addLocalSendRequest:(state, action) => {
            state.allRequests.obj.send = [
                ...state.allRequests.obj.send,
                action.payload
            ];
        },
    },
    extraReducers:(builder)=>{
        builders(builder, getFriends,'getFriends');
        builders(builder, getMyFriends,'getMyFriends');
        builders(builder, deleteMyFriend,'deleteMyFriend');
        builders(builder, requestToFriend,'requestToFriend');
        builders(builder, confirmFriendRequest,'confirmFriendRequest');
        builders(builder, friend, "friend");
        builders(builder, requestsToMe, "requestsToMe");
        builders(builder, cancelRequest, "cancelRequest");
        builders(builder, allRequests, "allRequests");
        builders(builder, friendSearchRequest, "friendSearchRequest");
    }
});

export const {modalDeleteFriendState, clearRequestToFriend, clearMyFriends, clearFriends,cancelLocalRequest,addLocalFriend, deleteLocalFriend, addLocalSendRequest, deleteLocalReceived} = friendsReducer.actions;

export default friendsReducer.reducer;