import styles from "./ChatNavigation.module.scss";
import ChatItem from "./ChatItem";
import Search from "../Icons/Search";
import Close from "../Icons/Close";
import BackIcon from "../Icons/BackIcon";
import PropTypes from 'prop-types';
import {NavLink, useParams} from 'react-router-dom';
import ChatLoader from "./ChatLoader";
import {checkSentStatus, checkReadStatus} from "../../utils/statusType";
import {useState} from "react";
import {searchChat} from "../../redux-toolkit/messenger/asyncThunk";
import {resetSearchChats, resetChatAndMessages} from "../../redux-toolkit/messenger/slice";
import {useDispatch, useSelector} from "react-redux";
import {checkContentType} from "../../utils/contentType";
import InputSearch from "../SearchUser/InputSearch";
import Loader from "../Loader/Loader";

export default function ChatNavigation({
  chatsStatus,
  chats,
  handleLoadChat,
  authUser,
  handleLoadPrevChats,
  handleLoadMoreChats,
  hasMore,
  pageNumber
}) {
  const dispatch = useDispatch();
  const {chatId} = useParams();
  const searchChats = useSelector(state => state.messenger.searchChats);

  const [searchText, setSearchText] = useState('');
  const [show, setShow] = useState(false);
  const handleGetSearchResult = (searchValue) => {
    dispatch(searchChat({input: searchValue, page: 0, size: 20}));
  };
  const handleResetSearchResult = () => {
    dispatch(resetSearchChats());
  };
  const handleBackIcon = () => {
    setSearchText('');
    setShow(false);
    handleResetSearchResult();
  };

  return (
    <section
      className={(window.location.pathname !== '/messages') ? `${styles.chatNavSection} ${styles.hide}` : styles.chatNavSection}>
      {chatsStatus === ("pending" || "") ? <ChatLoader/>
        :
        <>
          <div className={styles.chatNavSection__header}>
            <div className={styles.chatNavSection__header__text}>
              <h1>Chats</h1>
            </div>
            <NavLink to='/messages/new' onClick={()=> dispatch(resetChatAndMessages())}>
              <div className={styles.chatNavSection__header__buttonWrapper}>
                <img src='/img/chat.png' alt='chat'/>
              </div>
            </NavLink>
          </div>
          <div className={styles.chatNavSection__search}>
            {show && <div className={styles.chatNavSection__search__back} onClick={handleBackIcon}>
              <BackIcon size={"1em"}
              />
            </div>}
            <div className={styles.chatNavSection__search__input}>
              <span>
                <Search/>
              </span>
              <InputSearch 
                textSearch={searchText} 
                placeholder={"Search in Messenger"} 
                setTextSearch={setSearchText} 
                handleGetSearchResult={handleGetSearchResult} 
                handleResetSearchResult={handleResetSearchResult}
                openPortal={(e) => {
                  e.stopPropagation();
                  setShow(true);
                }}
                element={"chat-list"}
                closePortal={() => {setShow(false);}}
              />
              <span onClick={handleBackIcon}>
                <Close style={{cursor: "pointer"}}/>
              </span>
            </div>
          </div>
          <div className={styles.chatNavSection__chatList} id="chat-list">
            <ul className={styles.chatNavSection__chatList__items}>
              {!show
                ? chats.map(({id, chatParticipant, lastMessage}) => {
                  return (
                    <li key={id} className={styles.chatNavSection__chatList__items__item}>
                      <NavLink to={`/messages/${id}`} onClick={handleBackIcon}>
                        <ChatItem
                          additionalClass={styles.chatNavSection__chatList__items__item__avatar}
                          additionalClass2={styles.chatItemContainer__status__read}
                          name={chatParticipant.name + " " + chatParticipant.surname}
                          photo={chatParticipant.avatar}
                          message={lastMessage === null ? ""
                            : (lastMessage.sender.username === authUser.username
                              ?
                              ("You: " + checkContentType(lastMessage.contentType, lastMessage.content))
                              : checkContentType(lastMessage.contentType, lastMessage.content))}
                          isUnread={lastMessage !== null && lastMessage.sender.username !== authUser.username && checkSentStatus(lastMessage.status)}
                          isRead={lastMessage !== null && lastMessage.sender.username === authUser.username && checkReadStatus(lastMessage.status)}
                          clickHandler={() => {
                            handleLoadChat(id);
                            setShow(false);
                          }}
                          showTime={true}
                          time={lastMessage.createdAt}
                          chatItemClass={id == chatId ? styles.isActive : ''}
                          isMess={true}
                        />
                      </NavLink>
                    </li>
                  );
                }
                )
                :(
                  searchChats.status === '' ? <p className={styles.chatNavSection__chatList__items__res}>Enter name/surname</p>: 
                    ( searchChats.status === 'pending' ? <Loader/> :
                      (searchChats.obj.length === 0 ? <p className={styles.chatNavSection__chatList__items__res}>no results</p> :
                        searchChats.obj.map(({id, chatParticipant}) => {
                          return (
                            <li key={id} className={styles.chatNavSection__chatList__items__item}>
                              <NavLink to={`/messages/${id}`} onClick={handleBackIcon}>
                                <ChatItem
                                  additionalClass={styles.chatNavSection__chatList__items__item__avatar}
                                  name={chatParticipant.name + " " + chatParticipant.surname}
                                  photo={chatParticipant.avatar}
                                  message={null} clickHandler={() => {
                                    handleLoadChat(id);
                                  }}
                                />
                              </NavLink>
                            </li>
                          );
                        }))
                    )
                )
              }
            </ul>
            {<div className={styles.chatNavSection__chatList__pagination}>
              <button className={styles.chatNavSection__chatList__pagination__btn}
                onClick={handleLoadPrevChats} disabled={pageNumber === 0}>
                <BackIcon size={"24px"}/>
              </button>
              <button className={styles.chatNavSection__chatList__pagination__btn}
                onClick={handleLoadMoreChats} disabled={!hasMore}><BackIcon styleClass={styles.nextIcon}
                  size={"24px"}/>
              </button>
            </div>}
          </div>

        </>}
    </section>
  );
}
ChatNavigation.propTypes = {
  chatsStatus: PropTypes.oneOf(['', 'pending', 'fulfilled', 'rejected']).isRequired,
  chats: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      chatParticipant: PropTypes.shape({
        id: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
        surname: PropTypes.string.isRequired,
        username: PropTypes.string.isRequired,
        avatar: PropTypes.string,
      }).isRequired,
      lastMessage: PropTypes.shape({
        id: PropTypes.number.isRequired,
        contentType: PropTypes.string.isRequired,
        content: PropTypes.string.isRequired,
        sender: PropTypes.shape({
          id: PropTypes.number.isRequired,
          name: PropTypes.string.isRequired,
          surname: PropTypes.string.isRequired,
          username: PropTypes.string.isRequired,
          avatar: PropTypes.string,
        }).isRequired,
        status: PropTypes.string.isRequired,
      }),
    })
  ).isRequired,
  handleLoadChat: PropTypes.func.isRequired,
  authUser: PropTypes.object.isRequired,
  handleLoadPrevChats: PropTypes.func.isRequired,
  handleLoadMoreChats: PropTypes.func.isRequired,
  hasMore: PropTypes.bool,
  pageNumber: PropTypes.number
};