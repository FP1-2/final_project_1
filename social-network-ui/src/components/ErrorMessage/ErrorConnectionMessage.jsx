import styles from "./ErrorConnectionMessage.module.scss";
import PropTypes from 'prop-types';
import {useDispatch} from "react-redux";

export default function ErrorConnectionMessage({isVisible}) {
  const dispatch = useDispatch();

  function handleUpdateConnection() {
    dispatch({type: 'webSocket/connect'});
  }

  return (
    isVisible &&
    <div className={styles.errorMessage}>
      <p>Connection lost</p>
      <button onClick={handleUpdateConnection}>Update</button>
    </div>
  );
}
ErrorConnectionMessage.propTypes = {
  isVisible: PropTypes.bool.isRequired
};


