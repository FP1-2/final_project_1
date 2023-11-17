import { createSlice } from "@reduxjs/toolkit";
import initialValue from "./initialValue";
import builders from "../builders";
import { loadAuthToken, loadAuthUser } from "./thunks";

const loginReducer = createSlice({
  name: "auth",
  initialState: initialValue,
  reducers: {
    updateUser: (state, action) => {
      state.user.obj = action.payload;
    },
    clearTokenError: (state) => {
      state.token = { obj: {}, status: "", error: "" };
    },
    clearUserError: (state) => {
      state.user = { obj: {}, status: "", error: "" };
    },
  },

  extraReducers: (builder) => {
    builders(builder, loadAuthToken, "token");
    builders(builder, loadAuthUser, "user");
  },
});

export const { updateUser, clearTokenError, clearUserError } =
  loginReducer.actions;
export default loginReducer.reducer;
