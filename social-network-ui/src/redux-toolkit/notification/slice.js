import { createSlice } from '@reduxjs/toolkit';
import defaultInitialState from './defaultState';
import builders, { buildersPagination } from '../builders';
import {
    loadNotifications,
    loadNotification,
    notificationMarkAsRead,
    loadUnreadCount,
    updateFriendRequest
} from './thunks';
import { appendPaginationUtil } from "../../utils/utils";

const notificationReducer = createSlice({
    name: 'notifications',
    initialState: defaultInitialState,
    reducers: {
        appendNotifications: (state, action) => {
            appendPaginationUtil(state, action)
        },
        resetNotificationsState: (state) => {
            state.notifications = {
                ...defaultInitialState.notifications,
            };
        },
        markNotificationAsRead: (state, action) => {
            const notificationIndex = state.notifications.obj.content
                .findIndex((notification) => notification.id === action.payload);
            if (notificationIndex !== -1) {
                state.notifications.obj.content[notificationIndex].read = true;
            }
        },
        resetMarkAsRead: (state) => {
            state.mark_as_read = {
                ...defaultInitialState.mark_as_read,
            }
        },
        resetFriendRequest: (state) => {
            state.update_status_friend = {
                ...defaultInitialState.update_status_friend
            }
        },
        deleteFriendRequestNotification: (state, action) => {
            state.notifications.obj.content = state.notifications.obj.content.filter(
                notification => notification.id !== action.payload
            );
        },
        editNotificationQt: (state, action) => {
            state.unread_count.obj = state.unread_count.obj + action.payload;
        }
    },
    extraReducers: (builder) => {
        buildersPagination(builder, loadNotifications, 'notifications');
        builders(builder, notificationMarkAsRead, 'mark_as_read');
        builders(builder, updateFriendRequest, 'update_status_friend');
        builders(builder, loadNotification, 'notification');
        builders(builder, loadUnreadCount, 'unread_count');
    }
});

export const {
    appendNotifications,
    resetNotificationsState,
    markNotificationAsRead,
    resetMarkAsRead,
    resetFriendRequest,
    deleteFriendRequestNotification,
    editNotificationQt
} = notificationReducer.actions;

export default notificationReducer.reducer;
