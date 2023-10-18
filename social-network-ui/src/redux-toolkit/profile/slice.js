import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
// import { registrationThunkRequest,confirmRegistrationRequest } from "./thunks";
// import builders from "../builders";

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
    // extraReducers:(builder)=>{
    //     builders(builder, registrationThunkRequest,'registrationMassage');
    //     builders(builder, confirmRegistrationRequest,'confirmRegistrationMessage');
    // }
});

export const {modalDeleteFriendState,modalAddPostState,modalEditProfileState} = profileReducer.actions;

export default profileReducer.reducer;