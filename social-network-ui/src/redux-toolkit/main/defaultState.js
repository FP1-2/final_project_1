const defaultInitialState = {
    posts: {
        obj:  {
            content: [],
            pageable: {
                pageNumber: 0
            },
            totalPages: 0,
        },
        status: '',
        error: '',
    },
    recentSearch: []
};

export default defaultInitialState;
