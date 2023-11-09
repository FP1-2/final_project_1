import {createSlice} from '@reduxjs/toolkit';
import initialValue from './initialValue';
import builders, {buildersPagination} from '../builders';
import {loadNotifications, loadNotification, notificationMarkAsRead, loadUnreadCount} from './thunks';

const notificationReducer = createSlice({
    name: 'notifications',
    initialState: initialValue,
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
            state.notifications = {
                obj: {
                    content: [],
                    pageable: {
                        pageNumber: 0
                    },
                    totalPages: 0,
                    totalElements: 0,
                },
                status: '',
                error: '',
            };
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
    },
    extraReducers: (builder) => {
        buildersPagination(builder, loadNotifications, 'notifications');
        builders(builder, loadNotification, 'notification');
        builders(builder, notificationMarkAsRead, 'mark_as_read');
        builders(builder, loadUnreadCount, 'unread_count');
    }
});

export const {
    appendNotifications,
    resetNotificationsState,
    markNotificationAsRead,
    resetMarkAsRead,
} = notificationReducer.actions;

export default notificationReducer.reducer;
