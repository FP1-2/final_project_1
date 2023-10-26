import React from "react";
import styles from "./Header.module.scss";
import facebookIcon from "../../assets/facebook_icon.png";
import {NavLink} from "react-router-dom";

function Header() {

    return (
      <header className={styles.header}>
        <div>
          <NavLink to={"/"}>
              <img src={facebookIcon} alt="" />
          </NavLink>
          <input type="text" className={styles.searc} name="search" placeholder="Search on Facebook" />
        </div>
      </header>
    )
}

export default Header;
