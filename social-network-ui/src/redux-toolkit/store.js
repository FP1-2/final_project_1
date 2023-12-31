import {configureStore, combineReducers} from "@reduxjs/toolkit";
import {persistReducer, persistStore} from "redux-persist";
import storage from 'redux-persist/lib/storage';
import registrationReducer from "./registration/slice";
import loginReducer from "./login/slice";
import profileReducer from "./profile/slice";
import postReducer from "./post/slice";
import friendsReducer from "./friend/slice";
import favouritesReducer from "./favourite/slice";
import chatsReducer from "./messenger/slice";
import webSocketReducer from "./ws/slice"
import webSocketMiddleware from "./ws/webSocketMiddleware"
import notificationReducer from "./notification/slice"
import popupReducer from "./popup/slice"
import PostsInMainReducer from "./main/slice"
import resetPasswordSlice from "./ResetPassword/slice"
import passwordUpdateReducer from "./UpdatePassword/slice"
import groupsReducer from "./groups/slice"

const RESET_STATE = 'RESET_STATE';

const rootReducer = (state, action) => {
    if (action.type === RESET_STATE) {
        return {
            registration: undefined,
            auth: undefined,
            profile: undefined,
            post: undefined,
            friends: undefined,
            favourites: undefined,
            messenger: undefined,
            notifications: undefined,
            popup: undefined,
            postsInMain: undefined,
            groups: undefined,
        };
    }
    return combineReducers({
        registration: registrationReducer,
        resetPassword: resetPasswordSlice,
        passwordUpdate: passwordUpdateReducer,
        auth: loginReducer,
        profile:profileReducer,
        post: postReducer,
        friends: friendsReducer,
        favourites: favouritesReducer,
        messenger: chatsReducer,
        webSocket: webSocketReducer,
        notifications: notificationReducer,
        popup: popupReducer,
        postsInMain: PostsInMainReducer,
        groups: groupsReducer,
    })(state, action);
}

const persistConfig = {
    key: 'root',
    storage,
}

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({serializableCheck: false})
        .concat(webSocketMiddleware),
});

export const persist = persistStore(store);

export const logout = async () => {
    store.dispatch({type: 'webSocket/close'})
    store.dispatch({type: RESET_STATE});
    await persist.purge();
}

export const getToken = () => store.getState().auth.token.obj.token;

export default store;
