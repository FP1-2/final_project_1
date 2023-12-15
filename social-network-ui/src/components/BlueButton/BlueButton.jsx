import React from 'react';
import styles from './BlueButton.module.scss';
import PropTypes from "prop-types";

export default function  BlueButton({ text, onClick, className }){
  const buttonClassName = `${styles.blueButton} ${className || ''}`;
  return (
    <button className={buttonClassName} onClick={onClick}>
      {text}
    </button>
  );
}

BlueButton.propTypes = {
  text: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
  className: PropTypes.string,
};