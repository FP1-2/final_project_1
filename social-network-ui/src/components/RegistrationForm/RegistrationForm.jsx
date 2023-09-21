import React from "react";
import {Formik, Form} from 'formik';
import Input from "../Input/Input";
import style from "./RegistrationForm.module.scss";
import {object, string, ref} from "yup";
import { useDispatch, useSelector} from "react-redux";
import {registrationThunkRequest} from "../../redux toolkit/registration/thunks";


const validationSchema = object({
  name: string().required("Name is required").min(2, "Must be more than 1 characters"),

  surname: string().required("Surname is required").min(2, "Must be more than 1 characters"),

  email: string().email().required("E-mail is required"),

  password: string().required("Password id required!")
    .min(8, "Password must contain 8 or more characters")
    .matches(/[0-9]/, "Password must contain at least 1 digit character")
    .matches(/[a-z]/, "Password must contain at least 1 lowercase letter")
    .matches(/[A-Z]/, "Password must contain at least 1 uppercase letter")
    .matches(/^(?=.*[!@#$%^&*])/, "Password must contain at least 1 special character"),

  repeatPassword: string().required("Repeat your password")
    .oneOf([ref('password')], "Passwords does not match"),

});


const RegistrationForm = () => {

  const error = useSelector(state => state.registration?.data?.error);
  const dispatch = useDispatch();

  const initialValues = {
    name: "",
    surname: "",
    email: "",
    password: "",
    repeatPassword: ""
  };

  const onSubmit = async (values) => {
    const newObject = { ...values };

    delete newObject.repeatPassword;
    newObject.username = newObject.email;
    dispatch(registrationThunkRequest(newObject));
  };



  return (<Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema}>
    {({isValid}) => (
      <div className={style.formWrapper}>
        <Form className={style.form}>
          <h2 className={style.formTitle}>Registration</h2>
          <Input name='name' placeholder="Enter your name" type="text"/>
          <Input name="surname" placeholder="Enter your surname" type="text"/>
          <Input name="email" placeholder="Enter your e-mail" type='email'/>
          <div className={style.wrapperInputs}>
            <Input name="password" placeholder="Enter password" type="password"/>
            <Input name="repeatPassword" placeholder="Repeat password" type="password"/>
          </div>
          <button className={style.btnSubmit} disabled={!isValid} type="submit">Registration</button>
        </Form>
        {error ? <p className={style.error}>{error}</p> : null}
      </div>
    )}
  </Formik>);
};

export default RegistrationForm;