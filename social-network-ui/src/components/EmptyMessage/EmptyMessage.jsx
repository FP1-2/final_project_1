import React from 'react';
import style from './EmptyMessage.module.scss';
import PropTypes from "prop-types";
const EmptyMessage = ({ message }) => {
  return (
    <div className={style.styledMessage}>
      <p>{message}</p>
    </div>
  );
};
EmptyMessage.propTypes={
  message: PropTypes.string.isRequired
};
export default EmptyMessage;
