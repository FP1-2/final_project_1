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
import resetPasswordSlice from "./ResetPassword/slice"
import passwordUpdateReducer from "./UpdatePassword/slice"

const RESET_STATE = 'RESET_STATE';

const rootReducer = (state, action) => {
    if (action.type === RESET_STATE) {
        return {
            registration: undefined,
            auth: undefined,
            profile:undefined,
            post:undefined,
            friend:undefined,
            favourites:undefined,
            messenger: undefined

        };
    }
    return combineReducers({
        registration: registrationReducer,
        login: loginReducer,
        resetPassword: resetPasswordSlice,
        passwordUpdate: passwordUpdateReducer,

        auth: loginReducer,
        profile:profileReducer,
        post:postReducer,
        friends:friendsReducer,
        favourites:favouritesReducer,
        messenger: chatsReducer,
        webSocket: webSocketReducer
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
