const initialValue = {
    getFriends:{
        obj: [],
        status: '',
        error: '',
    },
    deleteMyFriend:{
        obj: {},
        status: '',
        error: '',
    },
    requestToFriend:{
        obj: {},
        status: '',
        error: '',
    },
    confirmFriendRequest:{
        obj: {},
        status: '',
        error: '',
    },
    friend:{
        obj: {},
        status: '',
        error: '',
    },
    requestsToMe:{
        obj: [],
        status: '',
        error: '',
    },
    cancelRequest:{
        obj: [],
        status: '',
        error: '',
    },
    allRequests:{
        obj: {
            send:[],
            received:[]
        },
        status: '',
        error: '',
    },
    modalDeleteFriend:false,
}
export default initialValue;