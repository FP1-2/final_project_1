import React, { useEffect } from 'react';
import {useDispatch, useSelector} from 'react-redux';
import styles from './PopupMessage.module.scss';
import {resetPopup} from "../../redux-toolkit/popup/slice";

function PopupMessage() {
  const { message, show } = useSelector((state) => state.popup);
  const dispatch = useDispatch();

  useEffect(() => {
    if (show) {
      const timer = setTimeout(() => {
        dispatch(resetPopup());
      }, 4000);
      return () => {
        clearTimeout(timer);
      };
    }
  }, [show]);

  if (!show) return null;

  return (
    <div className={`${styles.popup} ${show ? styles['popup_show'] : ''}`}>
      {message}
    </div>
  );
}

export default PopupMessage;
