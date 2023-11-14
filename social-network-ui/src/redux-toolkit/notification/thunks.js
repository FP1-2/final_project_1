import { createAsyncThunk } from '@reduxjs/toolkit';
import { workAx } from '../ax.js';
import {appendNotifications, markNotificationAsRead} from "./slice";

export const loadNotifications = createAsyncThunk(
    'notifications/notifications',
    async ({ page = 0, size = 10 }, { dispatch, rejectWithValue }) => {
        const params = new URLSearchParams({ page, size });
        try {
            const response = await workAx('get', `api/notifications?${params}`);
            if (page > 0) {
                dispatch(appendNotifications({
                    key: "notifications",
                    data: response.data
                }));
            } else {
                return response.data;
            }
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const loadNotification = createAsyncThunk(
    'notifications/notification',
    async (notificationId, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/notifications/${notificationId}`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const notificationMarkAsRead = createAsyncThunk(
    'notifications/mark_as_read',
    async (notificationId, { dispatch, rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/notifications/${notificationId}/mark-as-read`);
            if (response.status === 200) dispatch(markNotificationAsRead(notificationId));
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const loadUnreadCount = createAsyncThunk(
    'notifications/unread_count',
    async (notificationId, { rejectWithValue }) => {
        try {
            const response = await workAx("get",`api/notifications/unread-count`);
            return response.data;
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const updateFriendRequest = createAsyncThunk(
    'notifications/update_status_friend',
    async ({ userId, status }, { rejectWithValue }) => {
        try {
            const response = await workAx("put",`api/friends/update-status`, {
                userId,
                status,
            });
            if (response.status === 200) {
                if (status) {
                    return 'You have successfully accepted the request.';
                } else {
                    return 'You have deleted the friend request.';
                }
            }
        } catch (err) {
            return rejectWithValue(err.response.data);
        }
    }
);