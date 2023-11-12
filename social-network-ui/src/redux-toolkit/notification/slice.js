import {createSlice} from '@reduxjs/toolkit';
import defaultInitialState from './defaultState';
import builders, {buildersPagination} from '../builders';
import {loadNotifications,
        loadNotification,
        notificationMarkAsRead,
        loadUnreadCount,
        updateFriendRequest} from './thunks';

const notificationReducer = createSlice({
    name: 'notifications',
    initialState: defaultInitialState,
    reducers: {
        appendNotifications: (state, action) => {
            state.notifications.obj.content = [
                ...state.notifications.obj.content,
                ...action.payload.content
            ];
            state.notifications.obj.pageable.pageNumber = action.payload.pageable.pageNumber;
            state.notifications.obj.totalPages = action.payload.totalPages;
            state.notifications.obj.totalElements = action.payload.totalElements;
        },
        resetNotificationsState: (state) => {
            state.notifications = defaultInitialState.notifications;
        },
        markNotificationAsRead: (state, action) => {
            const notificationIndex = state.notifications.obj.content
                .findIndex((notification) => notification.id === action.payload);
            console.log("notificationIndex: " + notificationIndex);
            if (notificationIndex !== -1) {
                state.notifications.obj.content[notificationIndex].read = true;
            }
        },
        resetMarkAsRead: (state) => {
            state.mark_as_read = {
                obj: '',
                status: '',
                error: '',
            }
        },
        resetFriendRequest: (state) => {
            state.update_status_friend = {
                obj: '',
                status: '',
                error: '',
            }
        },
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
} = notificationReducer.actions;

export default notificationReducer.reducer;
