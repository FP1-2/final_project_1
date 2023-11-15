export function createHandleScroll({ spread= 20, scrollRef, status, fetchMore }){
    return () => {
        const scrollContainer = scrollRef.current;
        if (scrollContainer){
            const ct = scrollContainer.scrollTop;
            const ch = scrollContainer.clientHeight;
            const sh = scrollContainer.scrollHeight;
            //console.log(ct + " + " + ch + "+" + spread + "=>" + sh)
            if ((ct + ch + spread) >= sh && status !== 'pending') {
                //console.log(" start ")
                fetchMore();
            }
        }
    };
}

export function appendPaginationUtil(state, action) {
    const { key, data } = action.payload;
    if (!state[key]) {
        console.error(`No such state at path: state.${key}`);
        return;
    }
    state[key].obj.content.push(...data.content);
    state[key].obj.pageable = {...data.pageable};
}
