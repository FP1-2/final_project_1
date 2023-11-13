const initialValue = {
    getPost:{
        obj: {},
        status: '',
        error: '',
    },
    editPost:{
        obj: {},
        status: '',
        error: '',
    },
    savePost:{
        obj: {},
        status: '',
        error: '',
    },
    getCommentsPost: {
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
    addComment:{
        obj: {},
        status: '',
        error: '',
    },
    addLike:{
        obj: {},
        status: '',
        error: '',
    },
    addPost:{
        obj: {},
        status: '',
        error: '',
    },
    postObj:{},
    addRepost:{
        obj: {},
        status: '',
        error: '',
    },
    deletePost:{
        obj: {},
        status: '',
        error: '',
    },
    postsUser:{
        obj: {
            content: [],
            totalPages:0,
            pageable: {
              pageNumber:0,
            },
          },
        status: '',
        error: '',
    },
    modalEditPost:false,
    modalAddRepost:false,
    modalAddPost:false,
}
export default initialValue;