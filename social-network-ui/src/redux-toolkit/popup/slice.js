import { createSlice } from '@reduxjs/toolkit';

export const slice = createSlice({
    name: 'popup',
    initialState: {
            message: '',
            show: false
    },
    reducers: {
        showMessage: (state, action) => {
            state.message = action.payload;
            state.show = true;
        },
        resetPopup: (state) => {
                state.message = '';
                state.show = false;
        },
    }
});

export const { showMessage,  resetPopup} = slice.actions;
export default slice.reducer;