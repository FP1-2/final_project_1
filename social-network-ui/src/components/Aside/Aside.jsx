import {NavLink} from "react-router-dom";
import PostsIcon from "../../assets/postsIcon.png";
import MessagesIcon from "../../assets/messagesIcon.png";
import FavoritsIcon from "../../assets/favoriteIcon.png";
import NotificationIcon from "../../assets/notificationIcon.png";
import ProfileIcon from "../../assets/profileIcon.png";
import styles from "./Aside.module.scss";
import React from "react";
import {logout} from "../../redux-toolkit/store";
export default function Aside (){

    return (
        <aside className={styles.aside}>
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
    <button className={styles.logOut} onClick={() => logout()}>Log out</button>
        </aside>
    )
}