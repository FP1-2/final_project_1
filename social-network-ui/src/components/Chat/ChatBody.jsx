import React, {useRef} from 'react';
import Message from '../Message/Message';
import Avatar from '../Avatar/Avatar';
import Loader from '../Loader/Loader';
import {formatDate} from '../../utils/formatData';
import styles from './Chat.module.scss';
import PropTypes from 'prop-types';

export default function ChatBody({
  id,
  chat,
  messages,
  messagesList,
  authUser,
  newMess,
  handleMouseEnter,
  hasMore,
  handleLoadMoreMessages,
  handleOpenImg,
  pageNumber
}) {
  const isCreatingNewChat = id !== "new";
  const containerRef = useRef(null);
  const handleScroll = () => {
    const container = containerRef.current;
    if (hasMore && (container.scrollHeight-container.clientHeight+container.scrollTop)<2 && messages.status !== 'pending') {
      handleLoadMoreMessages();
    }
  };
  return (
    isCreatingNewChat &&
    <div className={`${messagesList.length === 0 ? styles.chat__emptyList : styles.chat__body}`}
      onMouseEnter={handleMouseEnter}
      ref={containerRef}
      onScroll={handleScroll}
    >
      {messagesList.length === 0 && messages.status === 'fulfilled' ? (
        chat.status === 'fulfilled' && (
          <>
            <Avatar
              photo={chat.obj.chatParticipant.avatar}
              name={chat.obj.chatParticipant.name + ' ' + chat.obj.chatParticipant.surname}
              additionalClass={styles.chat__body__emptyMessages}
            />
            <p
              className={styles.chat__emptyList__name}>{chat.obj.chatParticipant.name} {chat.obj.chatParticipant.surname}</p>
            <p className={styles.chat__emptyList__text}>Send your first message</p>
          </>
        )
      ) 
        :
        (
          <ul className={styles.chat__body__messList}>
            {messages.status === 'pending' && pageNumber === 0 && messagesList.length=== 0 ? <Loader/> :
              messagesList.map(({id, contentType, content, createdAt, sender, status}, index) => {
                const prevMessage = messagesList[index + 1];
                const showDate = !prevMessage || new Date(createdAt) - new Date(prevMessage.createdAt) > 15 * 60 * 1000;

                return (
                  <li key={id} className={styles.chat__body__messList__item}>
                    {showDate && <div className={styles.messageDate}>{formatDate(createdAt)}</div>}
                    <Message
                      contentType={contentType}
                      content={content}
                      authUser={authUser.username === sender.username}
                      datetime={formatDate(createdAt)}
                      photo={sender.avatar}
                      additionalClass=""
                      name={sender.name + ' ' + sender.surname}
                      status={newMess !== null && newMess.id === id ? newMess.status : status}
                      index={index}
                      handleOpenImg={handleOpenImg}
                    />
                  </li>
                );
              })}
            {messages.status === 'pending' && pageNumber !== 0 && <Loader/>}
          </ul>
         
        )
      }
    </div>
  );
}

ChatBody.propTypes = {
  id: PropTypes.string.isRequired,
  chat: PropTypes.object.isRequired,
  messages: PropTypes.object.isRequired,
  messagesList: PropTypes.array.isRequired,
  authUser: PropTypes.object.isRequired,
  newMess: PropTypes.object,
  handleMouseEnter: PropTypes.func.isRequired,
  hasMore: PropTypes.bool.isRequired,
  handleLoadMoreMessages: PropTypes.func.isRequired,
  handleOpenImg: PropTypes.func,
  pageNumber: PropTypes.number.isRequired
};