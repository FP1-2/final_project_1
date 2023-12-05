import React from 'react';
import styles from './BlueButton.module.scss';
import PropTypes from "prop-types";

const BlueButton = ({ text, onClick }) => {
  return (
    <button className={styles.blueButton} onClick={onClick}>
      {text}
    </button>
  );
};

BlueButton.propTypes = {
  text: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired
};

export default BlueButton;
