import React from "react";

import { NavLink } from "react-router-dom";
import styles from "./Header.module.scss";
import facebookIcon from "../../assets/facebook_icon.png";
import NotificationIcon from "../../assets/notificationIcon.png";
import PostsIcon from "../../assets/postsIcon.png";
import MessagesIcon from "../../assets/messagesIcon.png";
import ProfileIcon from "../../assets/profileIcon.png";
import FavoritsIcon from "../../assets/favoriteIcon.png";
import Cookie from "js-cookie";
import LogInForm from "../LogInForm/LogInForm";

  function logout() {
    localStorage.clear();
    Cookie.remove("token");
    }
function Header() {
    const isAuthorized =  localStorage.getItem('token');
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
              <NavLink to="/favorits">
                  <img src={FavoritsIcon} alt="" />
                  Favorits</NavLink>
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
           <LogInForm/>
           </>
    )
}

export default Header;
