import styles from './LoginPage.module.scss';
import LoginForm from '../../components/SignInForm/SignInForm.jsx';
import { ReactComponent as FacebookLogo } from '../../img/Facebook-Logo.wine.svg';

const LoginPage = () => {
  return (
    <section className={styles.container}>
      <div className={styles.wrapper}>
        <div className={styles['logo-slogan__wrapper']}>
          <div className={styles['facebookLogo__wrapper']}>
            <FacebookLogo className={styles.facebookLogo} />
          </div>
          <div className={styles.slogan__wrapper}>
            Facebook helps you connect and share with the people in your life.
          </div>
        </div>
        <div className={styles['login-form__wrapper']}>
          <LoginForm />
        </div>
      </div>
    </section>
  );
};
export default LoginPage;
