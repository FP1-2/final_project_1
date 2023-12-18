import style from "./GroupAvatar.module.scss";
import React from "react";
import PropTypes from "prop-types";

export default function GroupAvatar({pathImage, circle}) {
  let wrap = {};
  let circ = {};

  if (circle) {
    wrap = {
      borderRadius: 50,
      width: "2.7rem",
      height: "2.7rem",
    };

    circ = {
      borderRadius: 50,
      width: "2.5rem",
      height: "2.5rem",
      marginTop: "0.11rem",
      marginLeft: "0.101rem"
    };
  }

  return (
    <div className={style.imageWrapper} style={wrap}>
      <img src={pathImage} style={circ} alt="Group avatar"/>
    </div>
  );
}

GroupAvatar.propTypes = {
  pathImage: PropTypes.string,
  circle: PropTypes.bool,
};
