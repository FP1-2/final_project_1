const initialValue = {
    notifications:{
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
    notification:{
        obj: {},
        status: '',
        error: '',
    },
    mark_as_read:{
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

export default initialValue;
