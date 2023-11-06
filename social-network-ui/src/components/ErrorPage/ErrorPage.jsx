import React from "react";
import style from "./ErrorPage.module.scss";
import PropTypes from "prop-types";

const ErrorPage = ({ message, className }) => {
  return (
    <div className={`${style.messageWrapper} ${className}`}>
      <p className={style.message}>{message}</p>
    </div>
  );
};

ErrorPage.propTypes = {
  message: PropTypes.string.isRequired,
  className: PropTypes.string
};


export default ErrorPage;