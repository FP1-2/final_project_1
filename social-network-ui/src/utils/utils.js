export function createHandleScroll({ scrollRef, status, fetchMore }){
    return () => {
        const scrollContainer = scrollRef.current;
        if (scrollContainer && scrollContainer.scrollTop + scrollContainer.clientHeight + 20 >= scrollContainer.scrollHeight
            && status !== 'pending') {
            fetchMore();
        }
    };
}