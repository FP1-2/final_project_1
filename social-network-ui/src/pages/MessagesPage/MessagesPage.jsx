import styles from './MessagesPage.module.scss';
import ChatNavigation from "../../components/ChatNavigation/ChatNavigation";
import {useDispatch, useSelector, shallowEqual} from "react-redux";
import {loadChats, loadUnreadMessagesQt} from "../../redux-toolkit/messenger/asyncThunk";
import {useEffect, useMemo, useState} from "react";
import {updateChats, updateChatsLastMessage, resetChat, resetMessages} from '../../redux-toolkit/messenger/slice';
import {Outlet, useNavigate, useParams} from 'react-router-dom';
import {getDate} from "../../utils/formatData";
import {setNewMessage} from '../../redux-toolkit/ws/slice';
import {useLocation} from "react-router-dom";

export default function MessagesPage() {
  const dispatch = useDispatch();
  const chats = useSelector(state => state.messenger.chats);
  const authUser = useSelector(state => state.auth.user.obj, shallowEqual);
  const newMess = useSelector(state => state.webSocket.newMessage);
  const newStatus = useSelector(state => state.webSocket.messageWithNewStatus);
  const unreadQt = useSelector(state => state.messenger.unreadMessagesQt.obj);
  const PAGE_SIZE = 10;
  const navigate = useNavigate();
  const {chatId} = useParams();
  const location = useLocation();
  const showChat = location.pathname.startsWith('/messages/') && location.pathname.includes('/messages');
  const [hasMore, setHasMore] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);
  useEffect(() => {
    dispatch(loadChats({page: 0, size: PAGE_SIZE}));
    setHasMore(true);
  }, [dispatch]);

  const sortedChats = useMemo(() => {
    chats.obj.length < PAGE_SIZE && setHasMore(false);
    if (chats.status === 'fulfilled') {
      const filteredChats = chats.obj.filter(chat => chat.lastMessage);
      return [...filteredChats].sort((a, b) => getDate(b.lastMessage.createdAt) - getDate(a.lastMessage.createdAt));
    }
    return [];
  }, [chats]);

  function handleLoadPrevChats() {
    if (pageNumber > 0) {
      dispatch(loadChats({page: pageNumber - 1, size: PAGE_SIZE}));
      setPageNumber(p => p - 1);
      setHasMore(true);
    }
  }

  function handleLoadMoreChats() {
    if (chats.obj.length > 0) {
      dispatch(loadChats({page: pageNumber + 1, size: PAGE_SIZE}));
      setPageNumber(p => p + 1);
    }
  }

  // Load chat by ID
  const handleLoadChat = (id) => {
    navigate(`/messages/${id}`);
  };

  // Updating last message
  useEffect(() => {
    if (newMess !== null) {
      dispatch(updateChats(newMess));
      dispatch(setNewMessage(null));
    }
  }, [newMess]);
  // Updating status of message
  useEffect(() => {
    if (newStatus !== null && newStatus.status === "READ") {
      dispatch(updateChatsLastMessage(newStatus));
    }
  }, [newStatus]);
  // Show unread message qt
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
    if (showChat || chatId ==='new') {
      dispatch(resetChat);
      dispatch(resetMessages);
    }
  }, [location.pathname]);

  return (
    <div className={styles.messengerPage}>
      <ChatNavigation
        chatsStatus={chats.status}
        chats={sortedChats}
        handleLoadChat={handleLoadChat}
        authUser={authUser}
        handleLoadPrevChats={handleLoadPrevChats}
        handleLoadMoreChats={handleLoadMoreChats}
        hasMore={hasMore}
        pageNumber={pageNumber}
      />
      <section
        className={(!showChat) ? `${styles.chatFieldSection} ${styles.hide}` : styles.chatFieldSection}>
        {(!showChat) ?
          <div className={styles.chatFieldSection__empty}>
            <h1>Виберіть чат або почніть нову переписку</h1>
          </div>
          : <Outlet/>}
      </section>

    </div>
  );
}
