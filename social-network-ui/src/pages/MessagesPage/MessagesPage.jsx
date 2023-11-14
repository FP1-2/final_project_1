import styles from './MessagesPage.module.scss';
import ChatNavigation from "../../components/ChatNavigation/ChatNavigation";
import {useDispatch, useSelector} from "react-redux";
import {loadChats} from "../../redux-toolkit/messenger/asyncThunk";
import {useEffect, useMemo, useState} from "react";
import {updateChats, updateChatsLastMessage} from '../../redux-toolkit/messenger/slice';
import {Outlet, useNavigate} from 'react-router-dom';
import {getDate} from "../../utils/formatData";
import {setNewMessage} from '../../redux-toolkit/ws/slice';
import {useLocation} from "react-router-dom";
import {checkReadStatus} from "../../utils/statusType";

export default function MessagesPage() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const authUser = useSelector(state => state.auth.user.obj);
  const {chats, unreadMessagesQt} = useSelector(state => state.messenger);
  const {newMessage, messageWithNewStatus} = useSelector(state => state.webSocket);

  const showChat = location.pathname.startsWith('/messages/') && location.pathname.includes('/messages');
  const [hasMore, setHasMore] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);
  const PAGE_SIZE = 20;

  useEffect(() => {
    dispatch(loadChats({page: 0, size: PAGE_SIZE}));
    setHasMore(true);
  }, []);
  
  const sortedChats = useMemo(() => {
    chats.obj.length < PAGE_SIZE ? setHasMore(false) : setHasMore(true);
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
    if (newMessage !== null) {
      dispatch(updateChats(newMessage));
      dispatch(setNewMessage(null));
    }
  }, [newMessage]);
  // Updating status of message
  useEffect(() => {
    if (messageWithNewStatus !== null && checkReadStatus(messageWithNewStatus.status)) {
      dispatch(updateChatsLastMessage(messageWithNewStatus));
    }
  }, [messageWithNewStatus]);
  // Show unread message qt
  useEffect(() => {
    if (unreadMessagesQt.obj === 0) {
      document.title = `Messenger | Facebook`;
    } else {
      document.title = ` (${unreadMessagesQt.obj}) Messenger | Facebook`;
    }
  }, [unreadMessagesQt.obj]);

  
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
