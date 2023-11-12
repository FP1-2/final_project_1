export default function builders(builder, extraReducer, atState){
    builder
        .addCase(extraReducer.pending, state => {
            state[atState].status = 'pending';
        })
        .addCase(extraReducer.fulfilled, (state, action) => {
            state[atState].obj = action.payload;
            state[atState].status = 'fulfilled';
            state[atState].error = '';
        })
        .addCase(extraReducer.rejected, (state, action) => {
            state[atState].error = action.payload;
            state[atState].status = 'rejected';
        })
}

export function buildersPagination(builder, extraReducer, atState){
    builder
        .addCase(extraReducer.pending, state => {
            state[atState].status = 'pending';
        })
        .addCase(extraReducer.fulfilled, (state, action) => {
            if (action.meta.arg.page === 0) state[atState].obj = action.payload;
            state[atState].status = 'fulfilled';
            state[atState].error = '';
        })
        .addCase(extraReducer.rejected, (state, action) => {
            state[atState].error = action.payload;
            state[atState].status = 'rejected';
        })
}