const initialValue = {
    addToFavourites: {
        obj: {},
        status: '',
        error: '',
    },
    favouritesList: {
        status:"",
        obj: {
            content:[],
            totalPages:0,
            pageable: {
                pageNumber:0
            }
        },
        error: '',
    },
    isFavourite: {
        obj: {},
        status: '',
        error: '',
    },
    deleteFavourite: {
        obj: {},
        status: '',
        error: '',
    },
}
export default initialValue;