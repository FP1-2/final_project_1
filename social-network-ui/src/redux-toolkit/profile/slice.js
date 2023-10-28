import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { editUser,loadUserProfile} from "./thunks";
import builders from "../builders";

const profileReducer = createSlice({
    name: "profile",
    initialState: initialValue,
    reducers: {
        modalDeleteFriendState: (state, action) => {
            state.modalDeleteFriend.state = action.payload;
        },
        modalAddPostState: (state, action) => {
            state.modalAddPost.state = action.payload;
        },
        modalEditProfileState: (state, action) => {
            state.modalEditProfile.state = action.payload;
        },
        removeUser: (state) => {
            state.profileUser.obj = {};
        },
    },
    extraReducers:(builder)=>{
        builders(builder, editUser,'editUser');
        builders(builder, loadUserProfile,'profileUser');
        // builders(builder, postsUser,'postsUser');
    }
});

export const {modalDeleteFriendState,modalAddPostState,modalEditProfileState,removeUser} = profileReducer.actions;

export default profileReducer.reducer;