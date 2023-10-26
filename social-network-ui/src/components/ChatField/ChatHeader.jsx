import React from 'react';
import { NavLink } from 'react-router-dom';
import BackIcon from '../Icons/BackIcon';
import ChatItem from '../ChatNavigation/ChatItem';
import styles from './ChatField.module.scss';
import PropTypes from 'prop-types';

export default function ChatHeader({ id, chat, handleInputClick, handleInputChange, resetMessages,userSearch, show, searchUsers, handleCreateChat }) {
  const isCreatingNewChat = id === "new";

  return (
    <div className={styles.chat__header}>
      <NavLink to='/messages' className={styles.back} onClick={resetMessages}>
        <BackIcon size={"1em"} />
      </NavLink>
      {isCreatingNewChat ? (
        <>
          <h2>Кому: </h2>
          <input
            type="search"
            placeholder=""
            onChange={(e) => handleInputChange(e)}
            value={userSearch}
            onClick={handleInputClick}
          />
          {show && (
            <ul className={styles.chat__filteredUsers}>
              {searchUsers.status === 'fulfilled' &&
                searchUsers.obj.map(({ id, avatar, name, surname, username }) => (
                  <li key={id} onClick={() => handleCreateChat(username)} className={styles.chat__filteredUsers__item}>
                    <ChatItem photo={avatar} name={name + ' ' + surname} additionalClass={styles.chat__header__filter} />
                  </li>
                ))}
            </ul>
          )}
          </>
        
      ) : (
        chat.status === "fulfilled" && (
          <NavLink to={`/profile/${chat.obj.chatParticipant.id}`} className={styles.chat__header__user}>
            <ChatItem
              photo={chat.obj.chatParticipant.avatar}
              name={chat.obj.chatParticipant.name + " " + chat.obj.chatParticipant.surname}
              additionalClass={styles.card}
              chatItemClass={styles.chat__header__user__card}
            />
          </NavLink>
        )
      )}
    </div>
  );
}
ChatHeader.propTypes = {
    id: PropTypes.string.isRequired,
    chat: PropTypes.object.isRequired,
    handleInputClick: PropTypes.func.isRequired,
    userSearch: PropTypes.string.isRequired,
    show: PropTypes.bool.isRequired,
    searchUsers: PropTypes.object.isRequired,
    handleCreateChat: PropTypes.func.isRequired,
    handleInputChange: PropTypes.func.isRequired,
    resetMessages: PropTypes.func.isRequired
  };