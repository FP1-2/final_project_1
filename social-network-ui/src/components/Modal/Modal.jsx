import styles from "./Modal.module.scss";
import Close from "../Icons/Close";
import PropTypes from 'prop-types';
export default function Modal ({hideModal, children}) {

    return (
        <div className={styles.modal} onClick={hideModal}>
            <div className={styles.modal__content}>
                {children}
                <Close clickHadler={hideModal}/>
            </div>
        </div>
    )
}

Modal.propTypes={
    hideModal: PropTypes.func,
    children: PropTypes.node
}
