import React, { useEffect } from "react";
import { NavLink, useLocation,useSearchParams} from "react-router-dom";
import style from "./ConfirmRegistration.module.scss"
import { confirmRegistrationRequest } from "../../redux-toolkit/registration/thunks";
import { useDispatch, useSelector } from "react-redux";

const ConfirmRegistration = () => {
    const location = useLocation().search;
    const dispatch = useDispatch();
    const statusSubmit = useSelector(state => state.registration.confirmRegistrationMessage.status);
    const errorSubmit = useSelector(state => state.registration.confirmRegistrationMessage.error.message);
    const [searchParams] = useSearchParams();
    const email=searchParams.get("em");

    useEffect(() => {
        dispatch(confirmRegistrationRequest(location));
    }, [])


    return (
        <div className={style.messageWrapper}>
            {statusSubmit === "fulfilled" ?
                <>
                    <p className={style.message}>The user whose email is <span className={style.email}>{email}</span> has been successfully registered!</p>
                    <NavLink to="/login" className={style.loginLink}>Log In</NavLink>
                </>
                : errorSubmit==="Invalid token or user not found"? <p className={style.message}>{errorSubmit}</p>:null}
        </div>
    );
}
export default ConfirmRegistration;