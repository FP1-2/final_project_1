import { createAsyncThunk } from '@reduxjs/toolkit';
import { workAx } from '../ax.js';
import {appendNotifications} from "./slice";

export const loadNotifications = createAsyncThunk(
    'notifications/notifications',
    async ({ page = 0, size = 10 }, { dispatch, rejectWithValue }) => {
        const params = new URLSearchParams({ page, size });
        try {
            const response = await workAx('get', `api/notifications?${params}`);
            if (page > 0) {
                dispatch(appendNotifications(response.data));
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
    async (notificationId, { rejectWithValue }) => {
        try {
            const response = await workAx("post",`api/notifications/${notificationId}/mark-as-read`);
            return response.data;
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