import React from "react";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { NavLink } from "react-router-dom";
import styles from "./Header.module.scss";
import facebookIcon from "../../assets/facebook_icon.png";
import NotificationIcon from "../../assets/notificationIcon.png";
import PostsIcon from "../../assets/postsIcon.png";
import MessagesIcon from "../../assets/messagesIcon.png";
import ProfileIcon from "../../assets/profileIcon.png";
import FavoritsIcon from "../../assets/favoriteIcon.png";
import Cookie from "js-cookie";
import LoginForm from "../LoginForm/LoginForm";

import { loginThunk } from "../../redux-toolkit/login/thunks";
import {isTokenInLocalStorage, deleteTokenFromLocalStorage, deleteUserFromLocalStorage} from '../../utils/localStorageHelper';


function Header() {
  const [isAuthorized, setIsAuthorized] = useState(isTokenInLocalStorage());
  const dispatch = useDispatch();
  const logout=()=> {
    deleteTokenFromLocalStorage();
    deleteUserFromLocalStorage();
    Cookie.remove("authToken");
    setIsAuthorized(false);
  };
  
  const handleSubmit = (values) => {

    dispatch(loginThunk(values));
    setIsAuthorized(true);
  };
  if (isAuthorized){
    return (
      <header className={styles.header}>
        <div>
          <img src={facebookIcon} alt="" />
          <input type="text" className={styles.searc} name="search" placeholder="Search on Facebook" />
        </div>
        <nav>
          <ul>
            <li>
              <NavLink to="/">
                <img src={PostsIcon} alt="" />
                Posts
              </NavLink>
            </li>
            <li>
              <NavLink to="/messages">
                <img src={MessagesIcon} alt="" />
                  Messages</NavLink>
            </li>
            <li>
              <NavLink to="/favorites">
                <img src={FavoritsIcon} alt="" />
                  Favorites</NavLink>
            </li>
            <li>
              <NavLink to="/notifications">
                <img src={NotificationIcon} alt="" />
                Notifications
              </NavLink>
            </li>
            <li>
              <NavLink to="/profile">
                <img src={ProfileIcon} alt="" />
                  Profile</NavLink>
            </li>
          </ul>
        </nav>
        <button className={styles.logOut}onClick={() => logout()}>Log out</button>
      </header>
    );
  } return(
    <>
      <header className={styles.header}>
        <div>
          <img src={facebookIcon} alt="" />
          <input type="text" name="search" placeholder="Search on Facebook" />
        </div>
      
        <nav>
          <ul>
            <li>
              <NavLink to="/">
                <img src={PostsIcon} alt="" />
                Posts
              </NavLink>
            </li>
          </ul>
        </nav>
           
           
      </header>
      <div className={styles.loginFormContainer}>
        <LoginForm handleSubmit={handleSubmit} />
      </div>
    </>
  );
}

export default Header;
