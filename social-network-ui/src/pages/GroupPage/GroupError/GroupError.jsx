import style from "./GroupError.module.scss";
import {NavLink} from "react-router-dom";
import React from "react";
import PropTypes from "prop-types";

export default function GroupError({type, message}){
  return (
    <>
      <div className={style.error}>
        <h2>{type}</h2>
        {message}
        <NavLink to={"/"} className={style.homeLink}>
                    Home Page
        </NavLink>
      </div>
    </>
  );
}
GroupError.propTypes = {
  type: PropTypes.string,
  message: PropTypes.string,
};