import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {loadAuthToken, loadAuthUser} from '../../redux-toolkit/login/thunks';
import styles from "./LoginPage.module.scss";
import LoginForm from '../../components/LogInForm/LogInForm.jsx';
import {ReactComponent as FacebookLogo} from '../../img/FacebookLogo.svg';
import {startLogoutTimer} from "../../redux-toolkit/store";
import PropTypes from "prop-types";
import {Navigate} from "react-router-dom";

const LoginPage = ({isAuth}) => {
  const dispatch = useDispatch();
  const {id} = useSelector(state => state.auth.token.obj) || null;
  const handleSubmit = (values) => {
    dispatch(loadAuthToken(values));
    startLogoutTimer();
  };
  useEffect(() => {
    
    if (id) {
      // console.log(id)
      dispatch(loadAuthUser(id));
    }
  }, [id, dispatch]);
  return (
    isAuth ? <Navigate to="/" replace/> :
      <section className={styles.container}>
        <div className={styles.wrapper}>
          <div className={styles['logo-slogan__wrapper']}>
            <div className={styles['facebookLogo__wrapper']}>
              <FacebookLogo className={styles.facebookLogo}/>
            </div>
            <h2 className={styles.slogan__wrapper}>
              Facebook helps you connect and share with the people in your life.
            </h2>
          </div>
          <div className={styles['login-form__wrapper']}>
            <LoginForm handleSubmit={handleSubmit}/>
          </div>
        </div>
      </section>
  );
};

export default LoginPage;
LoginPage.propTypes = {
  isAuth: PropTypes.bool.isRequired
};

