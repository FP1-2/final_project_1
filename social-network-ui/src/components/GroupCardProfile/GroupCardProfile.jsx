import React from "react";
import style from "./GroupCardProfile.module.scss";
import { NavLink } from "react-router-dom";
import PropTypes from "prop-types";


const GroupCardProfile = ({ el }) => {
  return (
    <div className={style.groupCardWrapper}>
      <NavLink to={`/groups/${el.id}`} className={style.groupCard}>
        <img className={style.groupCardImg} src={el.imageUrl ? el.imageUrl : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="Avatar of group" />
        <div className={style.groupCardInformWrapper}>
          <h3 className={style.groupCardInformTitle}>{`${el.name}`}</h3>
          <p className={style.groupCardInformText}><span className={style.iconGlobe}/>{`${el.isPublic ? 'Public group' : 'Private group'} · ${el.memberCount} members`}</p>
        </div>
      </NavLink>
    </div>
  );
};

GroupCardProfile.propTypes = {
  el: PropTypes.object.isRequired,
};


export default GroupCardProfile;