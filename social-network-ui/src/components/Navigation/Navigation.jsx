import {useLocation} from "react-router-dom";
import styles from "./Navigation.module.scss";
import React from "react";
import {logout} from "../../redux-toolkit/store";
import MainIcon from "../Icons/MainIcon";
import Favorites from "../Icons/Favorites";
import Friends from "../Icons/Friends";
import Exit from "../Icons/Exit";
import Avatar from "../Avatar/Avatar";
import NavItem from "./NavItem";
import PropTypes from "prop-types";

export default function Navigation({authUser}) {
  const location = useLocation();

  return (
    <nav className={styles.nav}>
      <ul className={styles.nav__list}>
        <NavItem to={`/profile/${authUser.id}`} label={`${authUser.name} ${authUser.surname}`}
          active={location.pathname === '/profile/' + authUser.id} isDesktop={true}>
          <Avatar name={authUser.name} photo={authUser.avatar}
            additionalClass={`${styles.nav__list__item__link__user} ${styles.mobileNone}`}/>
        </NavItem>

        <NavItem to="/" label="Main" icon="/img/main.png" active={location.pathname === '/'} isDesktop={false}>
          <MainIcon className={styles.desktopNone}/>
        </NavItem>

        <NavItem to="/friends" label="Friends" icon="/img/friends.png" isDesktop={false}
          active={location.pathname.includes("/friends")}>
          <Friends className={styles.desktopNone}/>
        </NavItem>

        <NavItem to="/messages" label="Messenger" icon="/img/messenger.png" isDesktop={true}
          active={location.pathname.includes("/messages")}>
        </NavItem>

        <NavItem to="/groups" label="Groups" icon="/img/groups.png" isDesktop={true}
          active={location.pathname.includes("/groups")}>
        </NavItem>

        <NavItem to="/favorites" label="Favorites" icon="/img/saved.png" isDesktop={false}
          active={location.pathname.includes("/favorites")}>
          <Favorites className={styles.desktopNone}/>
        </NavItem>

        <NavItem to="/notifications" label="Notifications" icon="/img/notification.png" isDesktop={true}
          active={location.pathname.includes("/notifications")}>
        </NavItem>

        <li className={styles.nav__list__item}>
          <button className={styles.nav__list__item__link} onClick={() => logout()}>
            <Exit className={styles.nav__list__item__link__exit}/>
            <span>Exit</span>
          </button>
        </li>
      </ul>
    </nav>
  );
}
Navigation.propTypes = {
  authUser: PropTypes.shape({
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
    surname: PropTypes.string.isRequired,
    username: PropTypes.string.isRequired,
    avatar: PropTypes.oneOfType([PropTypes.string, PropTypes.oneOf([null])]).isRequired,
  }).isRequired,
};