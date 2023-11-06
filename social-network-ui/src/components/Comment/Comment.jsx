import React from "react";
import style from "./Comment.module.scss";
import PropTypes from "prop-types";
import { NavLink } from "react-router-dom";

const Comment = ({ el }) => {
  return (
    <div className={style.commentWrapper}>
      <img className={style.commentImg} src={el.appUser.avatar
        ? el.appUser.avatar
        : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" />
      <div className={style.commentWrapperText}>
        <NavLink to={`/profile/${el.appUser.userId}`} className={style.commentTextTitle}>
          {el.appUser.name}
        </NavLink>
        <p className={style.commentText}>{el.content}</p>
      </div>
    </div>
  );
};


Comment.propTypes = {
  el: PropTypes.object.isRequired,
};
export default Comment;