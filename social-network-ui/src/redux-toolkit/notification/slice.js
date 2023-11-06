import {createSlice} from '@reduxjs/toolkit';
import initialValue from './initialValue';
import builders from '../builders';
import { loadNotifications, loadNotification, notificationMarkAsRead, loadUnreadCount } from './thunks';

const notificationReducer = createSlice({
    name: 'notifications',
    initialState: initialValue,
    reducers: {

    },
    extraReducers: (builder) => {
        builders(builder, loadNotifications, 'notifications');
        builders(builder, loadNotification, 'notification');
        builders(builder, notificationMarkAsRead, 'mark_as_read');
        builders(builder, loadUnreadCount, 'unread_count');
    }
});

export const {

} = notificationReducer.actions;

export default notificationReducer.reducer;
