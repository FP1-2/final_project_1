import  React, { useState } from 'react';
import {useDispatch} from "react-redux";
import {resetThunkRequest} from "../../redux-toolkit/ResetPassword/thunks.js";
import { object, string } from "yup";
import { Formik, Form } from "formik";
import { NavLink } from "react-router-dom";
import Input from '../../components/Input/Input.jsx';
import style from "./ResetPassword.module.scss";


const validationSchema = object({
  email: string().email().required('Please enter your Email'),
});

const ResetPassword = () => {
  const dispatch = useDispatch();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (values, { resetForm }) => {
    setIsSubmitting(true);
    setFormSubmitted(true);
    try {
      const email = values.email;
      const response = await dispatch(resetThunkRequest({ email }));

      console.log('Response:', response);

      if (response.type === 'resetPassword/data/fulfilled') {
        console.log('Password reset successful.');
        setSuccess(true);
        setError(null); // Очищаем ошибку
        resetForm();
      } else {
        console.log('Password reset failed.');
        setError('User not found. Please check your email and try again.');
      }
    } catch (error) {
      console.error('Error in handleSubmit:', error);
      setError('User not found. Please check your email and try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleReload = () => {
    window.location.reload();
  };

  return (
    <div className={style.reset}>
      {!formSubmitted ? (
        <Formik
          initialValues={{ email: '' }}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          <div className={style.reset}>
            <Form className={style.resetForm}>
              <div className={style.resetFormTitleWrapper}>
                <h2 className={style.resetFormTitle}>Forgot Password</h2>
                <p className={style.resetFormSubtitle}>
                  Enter your email address below to receive a link to reset your password.
                </p>
              </div>
              <Input name="email" placeholder="Enter your e-mail" type="email" />
              <NavLink to="/login" className={style.resetWrapperLink}>
                Back to login?
              </NavLink>
              <button className={style.resetBtnSubmit} type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Submitting...' : 'Reset now'}
              </button>
            </Form>
          </div>
        </Formik>
      ) : error ? (
        <div className={style.resetError}>
          {error}
          <button className={style.errorBtn} onClick={handleReload}>
            Reload
          </button>
        </div>
      ) : success ? (
        <div className={style.resetMessage}>Email sent! Check your inbox.</div>
      ) : null}
    </div>
  );
};

export default ResetPassword;