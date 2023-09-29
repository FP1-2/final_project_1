import React from "react";
import styles from "./LogInForm.module.scss"
import { Formik, Field, Form, ErrorMessage } from "formik";
import { NavLink } from "react-router-dom";
import * as Yup from "yup";
import { string } from 'yup';

const validationSchema = Yup.object().shape({
  email: string()
            .required('Email is required')
            .email('Invalid email format'),

  password: Yup.string().required("Enter the password"),
});

const LoginForm = () => {
  const handleSubmit = (values) => {
    console.log(values);
  };

  return (
    <div className={styles.root}>
      <Formik
        initialValues={{ email: "", password: "" }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        <Form>
          <div>
           
            <Field type="email" name="email" placeholder="Email address" />
            <ErrorMessage name="email" component="div" />
          </div>
          <div>
           
            <Field type="password" name="password" placeholder="Password" />
            <ErrorMessage name="password" component="div" />
          </div>
          <button type="submit" className={styles.submit}>Log In</button>
          <NavLink to="/forgotten password" className={styles.link}>Forgotten password?</NavLink>
          <button className={styles.create} >Create new account</button>
        </Form>
      </Formik>
    </div>
  );
};

export default LoginForm;
