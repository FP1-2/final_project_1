const builders = (builder, extraReducer, atState, obj) => {
    builder
        .addCase(extraReducer.pending, state => {
            state[atState].status = 'pending';
        })
        .addCase(extraReducer.fulfilled, (state, action) => {
            
            if (atState==='registration'){
                state[atState][obj] = action.meta.arg;
            }
            
            state[atState].status = 'fulfilled';
            state[atState].error = '';
        })
        .addCase(extraReducer.rejected, (state, action) => {
            state[atState].error = action.payload;
            state[atState].status = 'rejected';
        })
}

export default builders;