const initialValue = {
    getGroup:{
        obj: {
            id: null,
            createdDate: "",
            lastModifiedDate: "",
            imageUrl: "",
            name: "",
            description: "",
            memberCount: 0,
            isPublic: true,
            members: [],
            admins: [],
        },
        status: '',
        error: '',
    },
    userGroups:{
        obj: {
            content: [],
            totalPages: 0,
            pageable: {
                pageNumber: 0,
            },
        },
        status: '',
        error: '',
    },
    getPosts: {
        obj: {
            content: [],
            pageable: {},
        },
        status: '',
        error: '',
    }
}

export default initialValue;