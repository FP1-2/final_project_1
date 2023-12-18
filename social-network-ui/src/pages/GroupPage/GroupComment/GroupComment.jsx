import style from "../../../components/Comment/Comment.module.scss";
import {NavLink} from "react-router-dom";
import PropTypes from "prop-types";
import React from "react";

export default function GroupComment({ post }){
  return (
    <div className={style.commentWrapper}>
      <img className={style.commentImg} src={post.appUser.avatar
        ? post.appUser.avatar
        : "img/default-avatar.jpg"} alt="" />
      <div className={style.commentWrapperText}>
        <NavLink to={`/profile/${post.appUser.userId}`} className={style.commentTextTitle}>
          {post.appUser.name}
        </NavLink>
        <p className={style.commentText}>{post.content}</p>
      </div>
    </div>
  );
}

GroupComment.propTypes = {
  post: PropTypes.object.isRequired,
};