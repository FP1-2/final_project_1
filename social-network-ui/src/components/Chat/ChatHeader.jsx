import React, { useEffect, useState } from "react";
import { NavLink } from 'react-router-dom';
import BackIcon from '../Icons/BackIcon';
import ChatItem from '../ChatNavigation/ChatItem';
import styles from './Chat.module.scss';
import PropTypes from 'prop-types';
import { createPortal } from "react-dom";
import InputSearch from '../SearchUser/InputSearch';
import Loader from "../Loader/Loader";
export default function ChatHeader({
  id,
  chat,
  resetMessages,
  userSearch,
  setUserSearch,
  searchUsers,
  handleCreateChat,
  handleGetSearchResult,
  handleResetSearchResult
}) {
  const [showSearchPortal, setShowSearchPortal] = useState(false);
  const isCreatingNewChat = id === "new";

  const handleClickOutside = (e) => {
    const searchUserElement = document.getElementById('search-user-chat');
    if (searchUserElement && !searchUserElement.contains(e.target)) {
      setShowSearchPortal(false);
      setUserSearch('');
    }
  };
  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);
  const openPortal = () => {
    setShowSearchPortal(true);
  };
  useEffect(()=>{
    if(userSearch.length > 0) {
      openPortal();
    } else{
      setShowSearchPortal(false);
    }
  }, [userSearch]);
  return (
    <div className={styles.chat__header} id='chat__header'>
      <NavLink to='/messages' className={styles.back} onClick={() => {
        setShowSearchPortal(false);
        resetMessages;
      }}>
        <BackIcon size={"1em"} />
      </NavLink>
      {isCreatingNewChat ? (
        <>
          <h2>Кому: </h2>
          <InputSearch
            textSearch={userSearch}
            placeholder={"Enter user's name/surname"}
            setTextSearch={setUserSearch}
            handleGetSearchResult={handleGetSearchResult}
            handleResetSearchResult={handleResetSearchResult}
            openPortal={(e)=>{
              e.stopPropagation();
              openPortal;
            }}
            element={'search-user-chat'}
            closePortal={() => {setShowSearchPortal(false);}}
          />
          {showSearchPortal && createPortal(
            <ul className={styles.chat__filteredUsers} id="search-user-chat">
              {searchUsers.status === 'pending' ? <Loader/> 
                : (searchUsers.status === 'fullfiled' && (searchUsers.obj.length === 0 ? 
                  <p>no results</p>
                  : 
                  searchUsers.obj.map(({ id, avatar, name, surname, username }) => (
                    <li key={id} onClick={() => {
                      setShowSearchPortal(false);
                      handleCreateChat(username);
                    }}
                    className={styles.chat__filteredUsers__item}>
                      <ChatItem photo={avatar} name={name + ' ' + surname} additionalClass={styles.chat__header__filter} />
                    </li>
                  ))))}
            </ul>, document.getElementById("chat__header")
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
  userSearch: PropTypes.string.isRequired,
  searchUsers: PropTypes.object.isRequired,
  setUserSearch: PropTypes.func.isRequired,
  handleCreateChat: PropTypes.func.isRequired,
  handleGetSearchResult: PropTypes.func.isRequired,
  handleResetSearchResult: PropTypes.func.isRequired,
  resetMessages: PropTypes.func.isRequired
};