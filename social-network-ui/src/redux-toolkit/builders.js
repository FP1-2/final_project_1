const builders = (builder, extraReducer, atState) => {
    builder
        .addCase(extraReducer.pending, state => {
            state[atState].status = 'pending';
        })
        .addCase(extraReducer.fulfilled, (state, action) => {
            state[atState].obj = action.payload;
            state[atState].status = 'fulfilled';
            state[atState].err = '';
        })
        .addCase(extraReducer.rejected, (state, action) => {
            state[atState].err = action.payload;
            state[atState].status = 'rejected';
        })
}

export default builders;