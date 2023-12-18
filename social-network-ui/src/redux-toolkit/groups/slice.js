import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import {getGroup, getUserGroups} from "./thunks";
import builders, {buildersPagination} from "../builders";
import {appendPaginationUtil} from "../../utils/utils";
import {getPosts} from "./thunks";

const groupsReducer = createSlice({
    name: "groups",
    initialState: initialValue,
    reducers: {
        clearGroup: (state) => {
            state.getGroup = { ...initialValue.getGroup}
        },
        appendPostsInGroupPage: (state, action) => {
            appendPaginationUtil(state, action)
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
        buildersPagination(builder, getPosts, 'getPosts');
    }
});

export const {
    clearGroup,
    addUserGroups,
    appendPostsInGroupPage,
} = groupsReducer.actions;

export default groupsReducer.reducer;