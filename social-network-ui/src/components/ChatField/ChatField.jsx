import styles from './ChatField.module.scss';
import { useParams } from "react-router";
import { loadMessages, createChat, searchUser } from "../../redux-toolkit/messenger/asyncThunk";
import { addMessage, updateMessages, updateChats, updateChatsLastMessage, setMessagesList, setMessages, resetSearchUsers } from '../../redux-toolkit/messenger/slice';
import { setMessageWithNewStatus } from '../../redux-toolkit/ws/slice';
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import MessageInput from "./MessageInput"
import ChatHeader from './ChatHeader';
import ChatBody from './ChatBody';
import Modal from '../Modal/Modal'
import useDebounce from "../../utils/useDebounce";
export default function ChatField() {
    const { id } = useParams();
    const dispatch = useDispatch();
    const chat = useSelector(state => state.messenger.chat)
    const messages = useSelector(state => state.messenger.messages)
    const messagesList = useSelector(state => state.messenger.messagesList)
    const authUser = useSelector(state => state.messenger.user.obj)
    const searchUsers = useSelector(state => state.messenger.searchUsers)
    const { newMessage, messageWithNewStatus } = useSelector(state => state.webSocket);
    const isHovered = false;
    const [message, setMessage] = useState("");
    const [userSearch, setUserSearch] = useState("");
    const [hasMore, setHasMore] = useState(true);
    const [pageNumber, setPageNumber] = useState(0);
    const PAGE_SIZE = 10;
    const debounedSearchText = useDebounce(userSearch, 500);
    const [show, setShow] = useState(false);
    // const friends = [
    //     {id: 1,
    //         name: "Greak",
    //         surname: "Washington", 
    //         username: "Greak",
    //         avatar: null,
    //         },
    //     {id: 2,
    //         name: "Bret",
    //         surname: "Johnson", 
    //         username: "BretNickname",
    //         avatar: null,
    //         },
    //     {id: 3,
    //     name: "Ervin",
    //     surname: "Smith", 
    //     username: "ErvinNickname",
    //     avatar: null,
    //     },
    //     {id: 4,
    //         name: "Clem",
    //         surname: 'Jones', 
    //         username: 'ClemNickname',
    //         avatar: '',
    //     },
    //     {id: 5,
    //     name: 'Patty',
    //     surname: 'Williams', 
    //     username: 'PattyNickname',
    //     avatar: '',
    //     },    
    //     {id: 6,
    //     name: 'Kari',
    //     surname: 'Brown', 
    //     username: 'KariNickname',
    //     avatar: '',
    //     },
    //     {id: 7,
    //     name: 'Kam',
    //     surname: 'Taylor', 
    //     username: 'KamNickname',
    //     avatar: '',
    //     },
    //     {id: 9,
    //     name: 'Leo',
    //     surname: 'Miller', 
    //     username: 'LeoNickname',
    //     avatar: '',
    //     },
    //     {id: 10,
    //     name: 'Max',
    //     surname: 'Garcia', 
    //     username: "MaxNickname",
    //     avatar: '',
    //     },
    //     {id: 11,
    //     name:  "Glen",
    //     surname: "Wilson", 
    //     username: "GlenNickname",
    //     avatar: '',
    //     },
    // ]


    useEffect(() => {
        if (id !== "new") {
            dispatch(loadMessages({ id: id, page: 0, size: PAGE_SIZE }));
            dispatch(setMessages([]));
            setHasMore(true)
            setPageNumber(0)
        }
    }, [id, dispatch]);

    useEffect(() => {
        console.log(messagesList)
        dispatch(setMessagesList(messages.obj))
    }, [messages.obj])

    // Завантаження додаткових повідолень
    useEffect(() => {
        if (messages.obj.length !== 0 && messages.obj.length < PAGE_SIZE) {
            setHasMore(false)
        } else if (Object.keys(messages.obj).length > 0 && id !== messages.obj[0].chat.id) {
            setHasMore(true)
        }
    }, [messages.obj, id])

    const handleLoadMoreMessages = () => {
        dispatch(loadMessages({ id: id, page: pageNumber + 1, size: PAGE_SIZE }))
        setPageNumber(pageNumber + 1)
    };

    // Відправка повідомлень
    function handleMessageChange (e) {
        setMessage(e.target.value);
    }
    const sendMessage = (contentType, content) => {
        const chatMessage = {
            chatId: chat.obj.id,
            contentType: contentType,
            content: content
        }
        dispatch({ type: "webSocket/sendMessage", payload: chatMessage })
        setMessage('')
    }
    function emojiHandler(e) {
        setMessage(message + e.native)
    }
    useEffect(() => {
        if (newMessage !== null && Object.keys(chat).length > 0
            && chat.obj.id === newMessage.chat.id
            && newMessage.status !== "READ") {
            dispatch(addMessage(newMessage));

        }
    }, [newMessage])


    // зміна статусу повідомлень
    function changeMessageStatus() {
        const unreadMessages = messagesList.filter(m => {
            return m.sender.username !== authUser.username && m.status === "SENT"
        })

        unreadMessages.forEach(u => {
            dispatch({ type: "webSocket/updateMessageStatus", payload: u.id })
            dispatch(updateMessages(u))
            dispatch(updateChatsLastMessage(u))
        })

        const m = messagesList.find(u => {
            return u.chat.id === id && u.id === chat.obj.lastMessage.id
        })

        if (m) {
            m.status = "READ"
            dispatch(updateChats(m))
        }
    }

    useEffect(() => {
        if (messageWithNewStatus !== null && messageWithNewStatus.status !== "READ") {

            dispatch(updateMessages(messageWithNewStatus))
            dispatch(setMessageWithNewStatus(null))
        }
    }, [messageWithNewStatus])

    const handleMouseEnter = () => {
        changeMessageStatus()
    }
    // створення нового чату
    const handleCreateChat = (username) => {
        dispatch(createChat({ username }))
        setShow(false)
        setUserSearch('')
        setHasMore(false);
        setPageNumber(0);
        dispatch(resetSearchUsers());
    }
    // Пошук юзера для створення нового чату
    useEffect(() => {
        if(debounedSearchText.trim() === ''){
            dispatch(resetSearchUsers());
        }
        if (debounedSearchText) {
            handleUserChange(debounedSearchText);
        }
    }, [debounedSearchText]);

    const handleUserChange = (input) => {

        if (userSearch.trim() === '') {
            dispatch(resetSearchUsers());
            return;
        }
        dispatch(searchUser({ input: input, page: 0, size: PAGE_SIZE }))
    }
    const handleInputClick = () => {
        if (userSearch.trim() === '') {
            dispatch(resetSearchUsers());
        }
        setShow(true)
    }
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [imgSrc, setImgSrc] = useState(null)
    const handleOpenImg = (img) =>{
        setIsModalOpen(true);
        setImgSrc(img);
        console.log(isModalOpen)
    }
    return (
        <>
        <div className={styles.chat}>
            <ChatHeader
                id={id}
                chat={chat}
                handleInputClick={handleInputClick}
                handleInputChange={(e) => setUserSearch(e.target.value)}
                userSearch={userSearch}
                show={show}
                searchUsers={searchUsers}
                handleCreateChat={handleCreateChat}
                pageSize={PAGE_SIZE}
                resetMessages={()=> dispatch(setMessages([]))}
            />
            <ChatBody
                id={id}
                chat={chat}
                messages={messages}
                authUser={authUser}
                newMess={newMessage}
                messagesList={messagesList}
                handleMouseEnter={handleMouseEnter}
                hasMore={hasMore}
                handleLoadMoreMessages={handleLoadMoreMessages}
                isHovered={isHovered}
                handleOpenImg={handleOpenImg}
            />
            {id !== 'new' && id !== '' && <MessageInput
                sendMessage={sendMessage}
                handleMessageChange={handleMessageChange}
                message={message}
                emojiHandler={emojiHandler} />}
        </div>
        {isModalOpen && <Modal hideModal={()=>  setIsModalOpen(false)}>
        <img src={imgSrc} alt={imgSrc} onClick={e => e.stopPropagation() }
                    className={styles.modal__img} width={"auto"} height={"90%"}
                /></Modal>}
        </>
    )

}

