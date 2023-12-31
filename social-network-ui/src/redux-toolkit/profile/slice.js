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
        modalEditProfileState: (state, action) => {
            state.modalEditProfile.state = action.payload;
        },
        removeUser: (state) => {
            state.profileUser.obj = {};
        },
        resetEditProfileState: (state) =>{
            state.editUser = initialValue.editUser;
        }
    },
    extraReducers:(builder)=>{
        builders(builder, editUser,'editUser');
        builders(builder, loadUserProfile,'profileUser');
    }
});

export const {modalEditProfileState,removeUser,resetEditProfileState} = profileReducer.actions;

export default profileReducer.reducer;