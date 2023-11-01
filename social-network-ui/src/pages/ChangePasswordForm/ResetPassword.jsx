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
  
  const handleSubmit = async (values , { resetForm }) => {
    setIsSubmitting(true);
    setFormSubmitted(true);
    try {
      const email = values.email;
      await dispatch(resetThunkRequest({ email }));
      resetForm();
    } catch (error) {
      // Handle error
    } finally {
      setIsSubmitting(false); 
    }
     
  };
 

  return (
    <div className={style.reset}>
    {!formSubmitted ? (
        <Formik
          initialValues={{ email: "" }}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >

            <div className={style.reset}>
              <Form className={style.resetForm}>
                <div className={style.resetFormTitleWrapper}>
                  <h2 className={style.resetFormTitle}>Forgot Password</h2>
                  <p className={style.resetFormSubtitle}>
                    Enter your email address below to receive a link to reset
                    your password.
                  </p>
                </div>
                <Input name="email" placeholder="Enter your e-mail" type="email" />
                <NavLink to="/login" className={style.resetWrapperLink}>Back to login?</NavLink>
                <button
              className={style.resetBtnSubmit}
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Submitting...' : 'Reset now'}
            </button>
            
              </Form>
            </div>
        </Formik>
        ) : (
          <div className={style.resetMessage}>Email sent! Check your inbox.</div>
          )}
    </div>
  );
};

export default ResetPassword;