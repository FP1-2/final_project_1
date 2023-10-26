import React from "react";
import { useDispatch} from "react-redux";
import { loadAuthToken } from '../../redux-toolkit/login/thunks';
import styles from "./LoginPage.module.scss";
import LoginForm from '../../components/LogInForm/LogInForm.jsx';
import {ReactComponent as FacebookLogo} from '../../img/FacebookLogo.svg';

const LoginPage = () => {
  const dispatch = useDispatch();

  const handleSubmit = (values) => {

    dispatch(loadAuthToken(values));
  };

  return (
    <section className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles['logo-slogan__wrapper']}>
          <div className={styles['facebookLogo__wrapper']}>
            <FacebookLogo className={styles.facebookLogo} />
          </div>
          <h2 className={styles.slogan__wrapper}>
            Facebook helps you connect and share with the people in your life.
          </h2>
        </div>
        <div className={styles['login-form__wrapper']}>
          <LoginForm handleSubmit={handleSubmit} />
        </div>
      </div>
    </section>
  );
};

export default LoginPage;

