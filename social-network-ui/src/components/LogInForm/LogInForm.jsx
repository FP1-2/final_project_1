import React from "react";
import styles from "./LogInForm.module.scss";
import { Formik, Form } from "formik";
import { NavLink } from "react-router-dom";
import * as Yup from "yup";
import { string } from 'yup';
import PropTypes from 'prop-types';
import Input from '../Input/Input';
import {useSelector} from "react-redux";

const validationSchema = Yup.object().shape({
  username: string() 
    .required('E-mail is required'),
  password: Yup.string().required("Enter the password"),
});

const LoginForm = ({ handleSubmit }) => {
  const {status, error:{error}} = useSelector(state => state.auth.token);
  return (
    <div className={styles.root}>
      <Formik
        initialValues={{ username: "", password: "" }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        <Form>
          <div >
            <Input type="text" name="username" placeholder="E-mail" />
          </div>
          <div className={styles.passwordInputWrapper}>
            <Input type="password" name="password" placeholder="Password" />
          </div>
          {status==='rejected' && <div className={styles.error} >{error}</div>}
          <button type="submit" className={styles.submit}>Log In</button>
          <NavLink to="/reset-password" className={styles.link}>Forgotten password?</NavLink>
          <NavLink to="/registration" className={styles.create}>Create new account</NavLink>
        </Form>
      </Formik>
    </div>
  );
};

LoginForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
};

export default LoginForm;
