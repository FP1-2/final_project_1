const defaultInitialState = {
    notifications: {
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
    mark_as_read: {
        obj: '',
        status: '',
        error: '',
    },
    update_status_friend: {
        obj: '',
        status: '',
        error: '',
    },
    notification:{
        obj: {},
        status: '',
        error: '',
    },
    unread_count:{
        obj: {},
        status: '',
        error: '',
    },
};

export default defaultInitialState;
