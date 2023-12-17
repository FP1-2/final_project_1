import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getGroup, getUserGroups} from "./thunks";
import builders from "../builders";

const groupsReducer = createSlice({
    name: "groups",
    initialState: initialValue,
    reducers: {
        clearGroup: (state) => {
            state.getGroup = { ...initialValue.getGroup}
        },
        addUserGroups: (state, action) => {
            state.userGroups.obj.content = [
                ...state.userGroups.obj.content,
                ...action.payload.content
            ];
            state.userGroups.obj.pageable.pageNumber = action.payload.pageable.pageNumber;
            state.userGroups.obj.totalPages = action.payload.totalPages;
            state.userGroups.obj.totalElements = action.payload.totalElements;
        },
    },
    extraReducers:(builder)=>{
        builders(builder, getGroup, 'getGroup');
        builders(builder, getUserGroups, 'userGroups');
    }
});

export const {
    clearGroup,
    addUserGroups
} = groupsReducer.actions;

export default groupsReducer.reducer;