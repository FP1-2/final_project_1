const initialValue = {
    getGroup: {
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
    getPosts: {
        obj: {},
        status: '',
        error: '',
    }
}

export default initialValue;