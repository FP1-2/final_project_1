import style from "./GroupAvatar.module.scss";
import React from "react";
import PropTypes from "prop-types";

export default function  GroupAvatar({pathImage}){
  return(
    <div className={style.imageWrapper}>
      <img src={pathImage} alt="Group avatar" />
    </div>
  );
}

GroupAvatar.propTypes = {
  pathImage: PropTypes.string,
};
