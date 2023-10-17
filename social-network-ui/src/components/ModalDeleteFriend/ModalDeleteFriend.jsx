import React from "react";
import style from "./ModalDeleteFriend.module.scss";
// import PropTypes from "prop-types";
import { ReactComponent as Cross } from "../../img/cross.svg";

const ModalDeleteFriend = () => {
  return (
    <div className={style.modalWrapper}>
      <div className={style.modal}>
        <header className={style.modalHeader}>
          <h2 className={style.modalHeadertittle}>Remove Анна Надточий from friends</h2>
          <button className={style.modalHeaderBtn}>
            <Cross className={style.modalHeaderBtnImg}/>
          </button>
        </header>
        <main className={style.modalMain}>
          <p className={style.modalMainText}>Are you sure you want to remove Анна Надточий from your friends list?</p>
          <div className={style.modalMainBtnWrapper}>
            <button className={style.modalMainBtnFalse}>No</button>
            <button className={style.modalMainBtnTrue}>Сonfirm</button>
          </div>
        </main>
      </div>
    </div>
  );
};
// FriendProfile.propTypes = {
// //   file: PropTypes.object
// };
// FriendProfile.defaultProps = {
// //   file: {}
// };
export default ModalDeleteFriend;