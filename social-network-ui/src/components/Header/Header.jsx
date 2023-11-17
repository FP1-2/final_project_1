import React, {useEffect, useState} from "react";
import styles from "./Header.module.scss";
import {NavLink} from "react-router-dom";
import Avatar from "../Avatar/Avatar";
import PropTypes from "prop-types";
import FacebookIcon from "../Icons/FacebookIcon";
import NotificationIcon from "../Icons/NotificationIcon";
import MessengerIcon from "../Icons/Messenger";
import Search from "../Icons/Search";
import {useDispatch, useSelector} from "react-redux";
import {loadUnreadMessagesQt} from "../../redux-toolkit/messenger/asyncThunk";
import SearchUser from "../SearchUser/SearchUser";
import {createPortal} from "react-dom";
export default function Header({authUser, showMessageIcon}) {
  const dispatch = useDispatch();
  const unreadMessQt = useSelector(state => state.messenger.unreadMessagesQt.obj);
  const newNotificationsQt = 2;
  const [showSearchField, setShowSearchField] = useState(false);
  const [textSearch, setTextSearch] = useState("");
  function openSearchPortal (e){
    e.stopPropagation();
    setShowSearchField(true);
  }
  useEffect(() => {
    dispatch(loadUnreadMessagesQt());
  }, []);
  
  return (
    <header className={styles.header}>
      <div className={styles.header__logoWrap}>
        <NavLink to={"/"} className={styles.header__logoWrap__link}>
          <FacebookIcon size={"40px"} />
        </NavLink>
        <input type="text" className={styles.header__logoWrap__search} name="text" placeholder="Search on Facebook" 
          onClick={openSearchPortal} defaultValue="" />
        {showSearchField && createPortal(<SearchUser textSearch={textSearch} 
          setTextSearch={setTextSearch} handleBack={()=> {setShowSearchField(false);}}/>, document.body) }
      </div>
      <div className={styles.header__icons}>
        <div className={`${styles.header__icons__item} ${styles.desktopNone}`}  onClick={openSearchPortal}>
          <Search/>
        </div>
        {showMessageIcon && <NavLink to={'/messages'} className={styles.header__icons__item}>
          <MessengerIcon/>
          {unreadMessQt > 0 && <span>{unreadMessQt}</span>}
        </NavLink>}
        <NavLink to={'/notifications'} className={styles.header__icons__item}>
          <NotificationIcon/>
          {newNotificationsQt > 0 && <span>{newNotificationsQt}</span>}
        </NavLink>
        <NavLink to={`/profile/${authUser.id}`} className={styles.header__icons__item}>
          <Avatar name={authUser.name} photo={authUser.avatar} additionalClass={styles.header__icons__item__avatar}/>
        </NavLink>
      </div>

    </header>
  );
}

Header.propTypes = {
  authUser: PropTypes.shape({
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
    surname: PropTypes.string.isRequired,
    username: PropTypes.string.isRequired,
    avatar: PropTypes.oneOfType([PropTypes.string, PropTypes.oneOf([null])]).isRequired,
  }).isRequired,
  showMessageIcon: PropTypes.bool.isRequired
};