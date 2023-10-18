import React from "react";
import styles from "./LogInForm.module.scss";
import { Formik, Form } from "formik";
import { NavLink } from "react-router-dom";
import * as Yup from "yup";
import { string } from 'yup';
import PropTypes from 'prop-types';
import Input from '../Input/Input';

const validationSchema = Yup.object().shape({
  username: string() 
    .required('Username is required'),
  password: Yup.string().required("Enter the password"),
});

const LoginForm = ({ handleSubmit }) => {
  return (
    <div className={styles.root}>
      <Formik
        initialValues={{ username: "", password: "" }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        <Form>
          <div>
            <Input type="text" name="username" placeholder="Username" />
          </div>
          <div className={styles.passwordInputWrapper}>
            <Input type="password" name="password" placeholder="Password" />
          </div>
          <button type="submit" className={styles.submit}>Log In</button>
          <NavLink to="/forgotten password" className={styles.link}>Forgotten password?</NavLink>
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
