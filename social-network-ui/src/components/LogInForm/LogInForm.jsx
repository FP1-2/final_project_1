import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  clearTokenError,
  clearUserError,
} from "../../redux-toolkit/login/slice";
import { loadAuthToken } from "../../redux-toolkit/login/thunks";
import styles from "./LogInForm.module.scss";
import { Formik, Form } from "formik";
import { NavLink } from "react-router-dom";
import * as Yup from "yup";
import Input from "../Input/Input";
import Modal from "./Modal";

const validationSchema = Yup.object().shape({
  username: Yup.string()
    .email("Invalid email address")
    .required("Email is required"),
  password: Yup.string().required("Password is required"),
});

const LoginForm = () => {
  const dispatch = useDispatch();
  const tokenError = useSelector((state) => state.auth.token.error);
  const userError = useSelector((state) => state.auth.user.error);
  const [isModalVisible, setModalVisibility] = useState(false);

  const handleFormSubmit = (values) => {
    dispatch(loadAuthToken(values));
  };

  useEffect(() => {
    if (tokenError) {
      setModalVisibility(true);
    }

    if (userError) {
      setModalVisibility(true);
    }
  }, [tokenError, userError]);

  const handleCloseModal = () => {
    setModalVisibility(false);
    dispatch(clearTokenError());
    dispatch(clearUserError());
    localStorage.setItem("formModalVisible", "false");
  };

  useEffect(() => {
    const storedModalVisible = localStorage.getItem("formModalVisible");
    if (storedModalVisible === "false") {
      setModalVisibility(false);
    }
  }, []);

  const handleDocumentClick = (e) => {
    if (isModalVisible && !e.target.closest(`.${styles.modal}`)) {
      handleCloseModal();
    }
  };

  useEffect(() => {
    document.addEventListener("click", handleDocumentClick);
    return () => {
      document.removeEventListener("click", handleDocumentClick);
    };
  }, [isModalVisible]);

  return (
    <div className={styles.root}>
      <Formik
        initialValues={{ username: "", password: "" }}
        validationSchema={validationSchema}
        onSubmit={handleFormSubmit}
      >
        <Form>
          <div>
            <Input type="text" name="username" placeholder="E-mail" />
          </div>
          <div className={styles.passwordInputWrapper}>
            <Input type="password" name="password" placeholder="Password" />
          </div>
          <button type="submit" className={styles.submit}>
            Log In
          </button>
          <NavLink to="/reset-password" className={styles.link}>
            Forgotten password?
          </NavLink>
          <NavLink to="/registration" className={styles.create}>
            Create new account
          </NavLink>
        </Form>
      </Formik>

      {isModalVisible && (
        <Modal
          onClose={handleCloseModal}
          errorMessage="Oops, something went wrong!"
        >
          {tokenError && <p>{tokenError.message}</p>}
          {userError && <p>{userError.message}</p>}
        </Modal>
      )}
    </div>
  );
};

export default LoginForm;
