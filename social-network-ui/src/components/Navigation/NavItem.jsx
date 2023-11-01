import styles from "./Navigation.module.scss";
import React from "react";
import {NavLink} from "react-router-dom";
import PropTypes from 'prop-types';

export default function NavItem({to, label, icon, active, children, isDesktop}) {
  return (
    <li className={`${styles.nav__list__item} ${isDesktop && styles.mobileNone}`}>
      <NavLink to={to} className={`${styles.nav__list__item__link} ${active && styles.nav__list__item__link__active}`}>
        {icon && <img src={icon} alt={label} className={styles.mobileNone}/>}
        {children}
        {label && <span>{label}</span>}
      </NavLink>
    </li>
  );
};
NavItem.propTypes = {
  to: PropTypes.string.isRequired,
  label: PropTypes.string,
  icon: PropTypes.string,
  active: PropTypes.bool,
  children: PropTypes.node,
  isDesktop: PropTypes.bool.isRequired
};





