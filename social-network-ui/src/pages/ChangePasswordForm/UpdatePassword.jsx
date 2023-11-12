import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { updatePasswordThunk } from "../../redux-toolkit/UpdatePassword/thunks.js";
import { object, string, ref } from "yup";
import { Formik, Form } from "formik";
import {
  NavLink,
  useSearchParams,
  useLocation,
  useNavigate,
} from "react-router-dom";
import Input from "../../components/Input/Input";
import style from "./UpdatePassword.module.scss";

const validationSchema = object({
  password: string()
    .required("Password is required!")
    .min(8, "Password must contain 8 or more characters")
    .matches(/[0-9]/, "Password must contain at least 1 digit character")
    .matches(/[a-z]/, "Password must contain at least 1 lowercase letter")
    .matches(/[A-Z]/, "Password must contain at least 1 uppercase letter")
    .matches(
      /^(?=.*[!@#$%^&*])/,
      "Password must contain at least 1 special character"
    ),
  confirmPassword: string()
    .oneOf([ref("password"), null], "Passwords must match")
    .required("Confirm Password is required"),
});

const UpdatePassword = () => {
  const dispatch = useDispatch();
  const isUpdating = useSelector((state) => state.passwordUpdate.isUpdating);
  const updateErrorMessage = useSelector(
    (state) => state.passwordUpdate.updateErrorMessage
  );
  const [searchParams] = useSearchParams();
  const email = searchParams.get("em");
  const location = useLocation();
  const pathname = location.pathname;
  const partToRemove = "/change_password/";
  const token = pathname.replace(partToRemove, "");
  const navigate = useNavigate();

  const handleSubmit = (values) => {
    const newPassword = values.password;
    const confirmPassword = values.confirmPassword;
    if (newPassword !== confirmPassword) {
      // Passwords do not match
    } else {
      dispatch(updatePasswordThunk({ newPassword, email, token }));
      navigate("/login");
    }
  };

  const initialValues = {
    password: "",
    confirmPassword: "",
  };

  return (
    <div className={style.update}>
      <Formik
        initialValues={initialValues}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ isValid }) => (
          <Form className={style.updateForm}>
            <div className={style.updateFormTitleWrapper}>
              <h2 className={style.updateFormTitle}>Reset Password</h2>
              <p className={style.updateFormSubtitle}>
                {" "}
                To reset your password, enter a new one below. You will be
                logged in with your new password.
              </p>
            </div>
            <Input name="password" placeholder="Password" type="password" />
            <Input
              name="confirmPassword"
              placeholder="Confirm Password"
              type="password"
            />
            <NavLink to="/login" className={style.updateWrapperLink}>
              Back to Login
            </NavLink>
            <button
              className={style.updateBtnSubmit}
              type="submit"
              disabled={!isValid}
            >
              Submit
            </button>
          </Form>
        )}
      </Formik>
      {isUpdating && <p>Updating password...</p>}
      {updateErrorMessage && <p>Error: {updateErrorMessage}</p>}
    </div>
  );
};

export default UpdatePassword;
