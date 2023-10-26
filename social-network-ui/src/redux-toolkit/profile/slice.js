import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import { getUserThunkRequest,editUserThunkRequest} from "./thunks";
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
    },
    extraReducers:(builder)=>{
        builders(builder, getUserThunkRequest,'profileUser');
        builders(builder, editUserThunkRequest,'editUser');
    }
});

export const {modalDeleteFriendState,modalAddPostState,modalEditProfileState} = profileReducer.actions;

export default profileReducer.reducer;