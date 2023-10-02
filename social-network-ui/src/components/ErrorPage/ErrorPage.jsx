import React from "react";
import style from "./ErrorPage.module.scss";
import PropTypes from "prop-types";

const ErrorPage = ({ message }) => {

    return (
        <div className={style.messageWrapper}>
            <p className={style.message}>{message}</p>
        </div>
    );
};

ErrorPage.propTypes = {
    message: PropTypes.string.isRequired,
};


export default ErrorPage;