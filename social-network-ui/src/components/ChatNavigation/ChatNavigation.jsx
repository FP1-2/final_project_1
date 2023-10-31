import styles from "./ChatNavigation.module.scss";
import ChatItem from "./ChatItem";
import Search from "../Icons/Search";
import Close from "../Icons/Close";
import BackIcon from "../Icons/BackIcon";
import PropTypes from 'prop-types';
import {Link, NavLink, useParams} from 'react-router-dom';
import ChatLoader from "./ChatLoader";
import {checkSentType, checkReadType} from "../../utils/statusType";
import {useEffect, useState} from "react";
import useDebounce from "../../utils/useDebounce";
import {searchChat} from "../../redux-toolkit/messenger/asyncThunk";
import {resetSearchChats} from "../../redux-toolkit/messenger/slice";
import {useDispatch, useSelector} from "react-redux";
import {checkContentType} from "../../utils/contentType";

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
  const [searchText, setSearchText] = useState('');
  const [show, setShow] = useState(false);
  const debouncedSearchText = useDebounce(searchText, 500);
  const searchChats = useSelector(state => state.messenger.searchChats);
  const {chatId} = useParams();

  useEffect(() => {
    if (debouncedSearchText) {
      handleSearchChange(debouncedSearchText);
    }
  }, [debouncedSearchText]);
  const handleSearchChange = (input) => {

    if (searchText.trim() === '') {
      dispatch(resetSearchChats());
      return;
    }
    dispatch(searchChat({input: input, page: 0, size: 20}));
  };

  const handleInputClick = () => {
    if (searchText.trim() === '') {
      dispatch(resetSearchChats());
    }
    setShow(true);
  };

  const handleBackIcon = () => {
    setSearchText('');
    setShow(false);
    dispatch(resetSearchChats());
  };

  return (
    <section
      className={(window.location.pathname !== '/messages') ? `${styles.chatNavSection} ${styles.hide}` : styles.chatNavSection}>
      {chatsStatus === ("pending" || "") ? <ChatLoader/>
        :
        <>
          <div className={styles.chatNavSection__header}>
            <div className={styles.chatNavSection__header__text}>
              <h1>Чати</h1>
            </div>
            <NavLink to='/messages/new'>
              <div className={styles.chatNavSection__header__buttonWrapper}>
                <div/>
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
              <input type="text" placeholder="Поиск в Messenger"
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}
                onClick={handleInputClick}/>
              <span onClick={handleBackIcon}>
                <Close style={{cursor: "pointer"}}/>
              </span>
            </div>
          </div>
          <div className={styles.chatNavSection__chatList}>
            <ul className={styles.chatNavSection__chatList__items}>
              {!show
                ? chats.map(({id, chatParticipant, lastMessage}) => {
                  return (
                    <li key={id} className={styles.chatNavSection__chatList__items__item}>
                      <NavLink to={`/messages/${id}`}>
                        <ChatItem
                          additionalClass={styles.chatNavSection__chatList__items__item__avatar}
                          additionalClass2={styles.chatItemContainer__status__read}
                          name={chatParticipant.name + " " + chatParticipant.surname}
                          photo={chatParticipant.avatar}
                          message={lastMessage === null ? ""
                            : (lastMessage.sender.username === authUser.username
                              ?
                              ("Ви: " + checkContentType(lastMessage.contentType, lastMessage.content))
                              : checkContentType(lastMessage.contentType, lastMessage.content))}
                          isUnread={lastMessage !== null && lastMessage.sender.username !== authUser.username && checkSentType(lastMessage.status)}
                          isRead={lastMessage !== null && lastMessage.sender.username === authUser.username && checkReadType(lastMessage.status)}
                          clickHandler={() => {
                            handleLoadChat(id);
                            setShow(false);
                          }}
                          showTime={true}
                          time={lastMessage.createdAt}
                          chatItemClass={id == chatId ? styles.isActive : ''}
                        />
                      </NavLink>
                    </li>
                  );
                }
                )
                :
                searchChats.obj.map(({id, chatParticipant}) => {
                  return (
                    <li key={id} className={styles.chatNavSection__chatList__items__item}>
                      <Link to={`/messages/${id}`}>
                        <ChatItem
                          additionalClass={styles.chatNavSection__chatList__items__item__avatar}
                          name={chatParticipant.name + " " + chatParticipant.surname}
                          photo={chatParticipant.avatar}
                          message={null} clickHandler={() => {
                            handleLoadChat(id);
                          }}
                        />
                      </Link>
                    </li>
                  );
                })
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