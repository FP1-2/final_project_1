import styles from './MessagesPage.module.scss';
import ChatNavigation from "../../components/ChatNavigation/ChatNavigation";
import { useDispatch, useSelector } from "react-redux";
import { loadChat, loadChats, loadUnreadMessagesQt } from "../../redux-toolkit/messenger/asyncThunk";
import { useEffect, useMemo, useState } from "react";
import { updateChats, updateChatsLastMessage, addChat } from '../../redux-toolkit/messenger/slice';
import { Outlet, useNavigate } from 'react-router-dom';
import { useParams } from "react-router";
import { getDate } from "../../utils/formatData";
import { setNewMessage } from '../../redux-toolkit/ws/slice';
export default function MessagesPage() {
    const dispatch = useDispatch();
    const { id } = useParams();
    const chats = useSelector(state => state.messenger.chats);
    const chat = useSelector(state => state.messenger.chat.obj);
    const authUser = useSelector(state => state.messenger.user.obj);
    const newMess = useSelector(state => state.webSocket.newMessage);
    const newStatus = useSelector(state => state.webSocket.messageWithNewStatus);
    const unreadQt = useSelector(state => state.messenger.unreadMessagesQt.obj);
    // const [sortedChats, setSortedChats] = useState([]);
    const [isChatClicked, setIsChatClicked] = useState(false);
    const [pageNumber, setPageNumber] = useState(0);
    const PAGE_SIZE = 10;
    const navigate = useNavigate();
    useEffect(() => {
        dispatch(loadChats({ page: pageNumber, size: PAGE_SIZE }));
    }, [dispatch]);

    const handleLoadMoreChats = () => {
        dispatch(loadChats({ page: pageNumber+1, size: PAGE_SIZE }));
        setPageNumber((prev) => prev + 1)
    };

    // useEffect(() => {
    //     if (chats.status === 'fulfilled') {
    //         const filteredChats = chats.obj.filter(chat => chat.lastMessage);
    //         const sortedChats = [...filteredChats].sort((a, b) => getDate(b.lastMessage.createdAt) - getDate(a.lastMessage.createdAt));
    //         setSortedChats(sortedChats);
    //     }
    // }, [chats]);
    const sortedChats = useMemo(()=>{
        if (chats.status === 'fulfilled') {
            const filteredChats = chats.obj.filter(chat => chat.lastMessage);
            return [...filteredChats].sort((a, b) => getDate(b.lastMessage.createdAt) - getDate(a.lastMessage.createdAt));
        }
        return [];
    }, [chats])

    const handleLoadChat = (id) => {
        dispatch(loadChat({ id }));
        setIsChatClicked(true)
    };
    useEffect(() => {
        if (!isChatClicked && id && id !== 'new') {
            dispatch(loadChat({ id }));
        }
    }, [id, dispatch]);
    
    useEffect(() => {
        if (Object.keys(chat).length > 0) {
            addChat(chat);
            navigate(`/messages/${chat.id}`);
        }
    }, [chat]);
    
    useEffect(() => {
        if (newMess !== null) {
            dispatch(updateChats(newMess));
            dispatch(setNewMessage(null))
        }
    }, [newMess]);

    useEffect(() => {
        if (newStatus !== null && newStatus.status !== "READ") {
            dispatch(updateChatsLastMessage(newMess));
        }
    }, [newStatus]);

    useEffect(() => {
        dispatch(loadUnreadMessagesQt());
    }, [dispatch]);

    useEffect(() => {
        if (unreadQt === 0) {
            document.title = `Messenger | Facebook`;
        } else {
            document.title = ` (${unreadQt}) Messenger | Facebook`;
        }
    }, [unreadQt]);
    
    useEffect(() => {
        if (window.location.pathname === '/messages') {
            setIsChatClicked(false)
        } 
    }, [window.location.pathname])
    return (
        <div className={styles.messengerPage}>
            <ChatNavigation
                chatsStatus={chats.status}
                chats={sortedChats}
                handleLoadChat={handleLoadChat}
                authUser={authUser}
                isChatClick={isChatClicked}
                handleCreateChatClick={() => {
                    setIsChatClicked(true)
                }
                }
                handleLoadMoreChats={handleLoadMoreChats}
                pageSize={PAGE_SIZE}
            />
            <section className={(window.location.pathname === '/messages') ? `${styles.chatFieldSection} ${styles.hide}` : styles.chatFieldSection} >
                {   (window.location.pathname === '/messages')?
                    <div className={styles.chatFieldSection__empty}>
                        <h1 >Виберіть чат або почніть нову переписку</h1>
                    </div>
                    : <Outlet />}
            </section>

        </div>
    );
}
