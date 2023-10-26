import {Navigate} from "react-router-dom";
import PropTypes from "prop-types";
export default function ProtectedRoute({isAuth, content}){
    return isAuth ? content : <Navigate to='/login' replace/>;
}


ProtectedRoute.propTypes = {
    isAuth: PropTypes.bool.isRequired,
    content: PropTypes.node.isRequired
};