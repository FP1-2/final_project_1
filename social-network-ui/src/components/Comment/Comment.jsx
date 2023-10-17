import React from "react";
import style from "./Comment.module.scss";
// import PropTypes from "prop-types";

const Comment = () => {
  return (
    <div className={style.commentWrapper}>
      <img className={style.commentImg} src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" />
      <div className={style.commentWrapperText}>
        <a href="#" className={style.commentTextTitle}>
          Надточий Анна
        </a>
        <p className={style.commentText}>Дуже гарно!</p>
      </div>
    </div>
  );
};


// Comment.propTypes = {
//   file: PropTypes.object
// };
// Comment.defaultProps = {
//   file:{}
// };
export default Comment;