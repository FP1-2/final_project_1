const defaultInitialState = {
    chats: {
        obj: [
        //     {
        //     id: 1,
        //     chatParticipant:{
        //         id:2,
        //         name: "Tom",
        //         surname: "Hardi",
        //         username: "tom",
        //         avatar: null
        //     },
        //     lastMessage:{
        //         id:1,
        //         contentType: "text",
        //         content: "sjhdhs  fsfsdfsdfsdf  sdfsdfsdfsdfsd   dsfsdfsdfsdfs sdf",
        //         createdAt: "2022-10-11T13:24:00",
        //         sender: {
        //             id:2,
        //         name: "Tom",
        //         surname: "Hardi",
        //         username: "tom",
        //         avatar: null
        //         },
        //         status: 'sent'
        //     }
        // }
    ],
        status: '',
        err: '',
    },
    chat: {
        obj: {
            // id: 1,
            // chatParticipant:{
            //     id:2,
            //     name: "Tom",
            //     surname: "Hardi",
            //     username: "tom",
            //     avatar: null
            // },
            // lastMessage:{
            //     id:1,
            //     contentType: "text",
            //     content: "sjhdhs  fsfsdfsdfsdf  sdfsdfsdfsdfsd   dsfsdfsdfsdfs sdf",
            //     createdAt: "2022-10-11T13:24:00",
            //     sender: {
            //         id:2,
            //     name: "Tom",
            //     surname: "Hardi",
            //     username: "tom",
            //     avatar: null
            //     },
            //     status: 'sent'
            // }
        },
        status: '',
        err: '',
    },
    messages:{
        obj: [
            // {
            //     id: 3,
            //     chat:{
            //         id:1
            //     },
            //     sender:{
            //         id: 9,
            //         name: "K",
            //         username: "k",
            //         surname:"k",
            //         avatar: null
            //     },
            //     contentType: "text",
            //     content: "authuser",
            //     createdAt: "2022-10-11T13:24:00",
            //     status: "sent"
            // },
            // {
            //     id: 4,
            //     chat:{
            //         id:1
            //     },
            //     sender:{
            //         id:2,
            //     name: "Tom",
            //     surname: "Hardi",
            //     username: "tom",
            //     avatar: null
            //     },
            //     contentType: "text",
            //     content: "sender",
            //     createdAt: "2022-10-11T13:24:01",
            //     status: "READ"
            // }
        ],
        status: '',
        err: '',
    },
    messagesList:[],
    
    message:{},
    newMess:null,
    
    user:  {
         obj: {}, 
         status: '', 
         error: '' 
    },
    unreadMessagesQt:{
        obj: 0, 
        status: '', 
        error: '' 
   },
   searchChats: {
        obj:[],
        status:'',
        error: ''
   },
   searchUsers: {
    obj:[],
    status:'',
    error: ''
}
}
export default defaultInitialState;