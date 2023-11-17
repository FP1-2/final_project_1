import React from "react";
import PropTypes from "prop-types";
import styles from "./Modal.module.scss";

const Modal = ({ onClose, errorMessage }) => {
  const handleClose = () => {
    onClose();
  };
  return (
    <div className={styles.modal}>
      <div className={styles.modalContent}>
        {errorMessage && <p className={styles.error}>{errorMessage}</p>}
        <button className={styles.closeButton} onClick={handleClose}>
          Close
        </button>
      </div>
    </div>
  );
};

Modal.propTypes = {
  onClose: PropTypes.func.isRequired,
  errorMessage: PropTypes.string,
};

export default Modal;
