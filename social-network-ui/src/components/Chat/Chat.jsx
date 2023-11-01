import styles from './Chat.module.scss';
import {useParams} from "react-router";
import {useNavigate} from 'react-router-dom';
import {loadMessages, createChat, searchUser, loadChat} from "../../redux-toolkit/messenger/asyncThunk";
import {
  updateChats,
  updateChatsLastMessage,
  resetSearchUsers,
  resetMessages,
  resetChat
} from '../../redux-toolkit/messenger/slice';
import {setMessageWithNewStatus} from '../../redux-toolkit/ws/slice';
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import MessageInput from "./MessageInput";
import ChatHeader from './ChatHeader';
import ChatBody from './ChatBody';
import Modal from '../Modal/Modal';
import useDebounce from "../../utils/useDebounce";
import {checkReadStatus, checkSentStatus, StatusType} from "../../utils/statusType";

export default function Chat() {
  const {chatId} = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const {chat, messages, searchUsers} = useSelector(state => state.messenger);
  const user = useSelector(state => state.auth.user.obj);
  const {newMessage, messageWithNewStatus} = useSelector(state => state.webSocket);

  const [messagesList, setMessagesList] = useState([]);

  const [hasMore, setHasMore] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);

  const [message, setMessage] = useState("");
  const [userSearch, setUserSearch] = useState("");
  const debounedSearchText = useDebounce(userSearch, 500);
  const [show, setShow] = useState(false);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [imgSrc, setImgSrc] = useState(null);
  const [newChat, setNewChat] = useState(false);
  const PAGE_SIZE = 10;
  /* Load Chat*/
  useEffect(() => {
    dispatch(resetChat());
    
    if (chatId !== 'new' && chatId) {
      dispatch(loadChat({id: chatId}));
      dispatch(resetMessages());
      dispatch(loadMessages({id: chatId, page: 0, size: PAGE_SIZE}));
      setNewChat(true);
      setPageNumber(0);
    }
  }, [chatId,]);
  
  useEffect(() => {
    if (!newChat) {
      const uniqueNewMessages = messages.obj.filter(message => {
        return !messagesList.some(existingMessage => existingMessage.id === message.id);
      });
      setMessagesList(prev => [...prev, ...uniqueNewMessages]);
    } else {
      setMessagesList(messages.obj);
      setNewChat(false);
    }
    const more = messages.obj.length >= PAGE_SIZE;
    setHasMore(more);
  }, [messages.obj]);

  /* Load More messages*/

  const handleLoadMoreMessages = () => {
    dispatch(loadMessages({id: chatId, page: pageNumber + 1, size: PAGE_SIZE}));
    setPageNumber(prev => prev + 1);
  };

  /*Sending new message*/
  function handleMessageChange(e) {
    setMessage(e.target.value);
  }

  const sendMessage = (contentType, content) => {
  
    const chatMessage = {
      chatId: chat.obj.id,
      contentType: contentType,
      content: content
    };
    dispatch({type: "webSocket/sendMessage", payload: chatMessage});
    setMessage('');
      
  };

  function emojiHandler(e) {
    setMessage(message + e.native);
  }

  useEffect(() => {
    if (newMessage !== null && Object.keys(chat).length > 0
      && chat.obj.id === newMessage.chat.id
      && !checkReadStatus(newMessage.status)) {
      setMessagesList(prevMessages => [newMessage, ...prevMessages]);
    }
  }, [newMessage]);


  /*change message status*/
  function changeMessageStatus() {
    const unreadMessages = messagesList.filter(m => {
      return m.sender.username !== user.username && checkSentStatus(m.status);
    });

    unreadMessages.forEach(u => {

      dispatch({type: "webSocket/updateMessageStatus", payload: u.id});
      dispatch(updateChatsLastMessage(u));
      setMessagesList(prevMessages => prevMessages.map(m =>
        m.id === u.id ? {...m, status: StatusType.READ} : m
      ));

    });

    const m = messagesList.find(u => {
      return u.chat.id === chatId && u.id === chat.obj.lastMessage.id;
    });

    if (m) {
      m.status = StatusType.READ;
      dispatch(updateChats(m));
    }
  }

  useEffect(() => {
    if (messageWithNewStatus !== null && checkReadStatus(messageWithNewStatus.status)) {
      setMessagesList(prevMessages => prevMessages.map(m =>
        m.id === messageWithNewStatus.id ? {...m, status: StatusType.READ} : m
      ));

      dispatch(setMessageWithNewStatus(null));
    }
  }, [messageWithNewStatus]);

  const handleMouseEnter = () => {
    changeMessageStatus();
  };
  const [createNewChat, setCreateNewChat] = useState(false);
  /* Creating new Chat*/
  const handleCreateChat = (username) => {
    dispatch(createChat({username}));
    setShow(false);
    setUserSearch('');
    setHasMore(false);
    setPageNumber(0);
    dispatch(resetSearchUsers());
    dispatch(resetMessages());
    setCreateNewChat(true);
  };
  useEffect(() => {
    if (createNewChat && chat.status === 'fulfilled') {
      navigate(`/messages/${chat.obj.id}`);
      setCreateNewChat(false);
    }
  }, [chat]);
  /* Search user for creating new Chat*/
  useEffect(() => {
    if (debounedSearchText.trim() === '') {
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
    dispatch(searchUser({input: input, page: 0, size: PAGE_SIZE}));
  };
  const handleInputClick = () => {
    if (userSearch.trim() === '') {
      dispatch(resetSearchUsers());
    }
    setShow(true);
  };

  const handleOpenImg = (img) => {
    setIsModalOpen(true);
    setImgSrc(img);
  };
  useEffect(() => {
    if (chat.status ==='rejected' && messages.status === 'rejected' &&
        chat.error.status === 404 || messages.error.status === 404 ||
        chat.error.status === 400 || messages.error.status === 400) {
      dispatch(resetChat());
      dispatch(resetMessages());
      navigate('/not-found');
      return;
    }
  }, [chat.error, messages.error]);
  
  return (
    (
      <>
        <div className={styles.chat}>
          <ChatHeader
            id={chatId}
            chat={chat}
            handleInputClick={handleInputClick}
            handleInputChange={(e) => setUserSearch(e.target.value)}
            userSearch={userSearch}
            show={show}
            searchUsers={searchUsers}
            handleCreateChat={handleCreateChat}
            pageSize={PAGE_SIZE}
            resetMessages={() => setMessagesList([])}
          />
          <ChatBody
            id={chatId}
            chat={chat}
            messages={messages}
            authUser={user}
            newMess={newMessage}
            messagesList={messagesList}
            handleMouseEnter={handleMouseEnter}
            hasMore={hasMore}
            handleLoadMoreMessages={handleLoadMoreMessages}
            handleOpenImg={handleOpenImg}
            pageNumber={pageNumber}
          />
          {chatId !== 'new' && chatId !== '' && <MessageInput
            sendMessage={sendMessage}
            handleMessageChange={handleMessageChange}
            message={message}
            emojiHandler={emojiHandler}/>}
        </div>
        {isModalOpen && <Modal hideModal={() => setIsModalOpen(false)}>
          <img src={imgSrc} alt={imgSrc} onClick={e => e.stopPropagation()}
            className={styles.modalImg}
          /></Modal>}
      </>)
  );
}

