import React, {useEffect,useState} from "react";
import style from "./ModalDeleteFriend.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import {useDispatch,useSelector} from "react-redux";
import { modalDeleteFriendState } from "../../redux-toolkit/profile/slice";

const ModalDeleteFriend = () => {
  const [scroll,setScroll]=useState(null);

  const handleScroll = () => {
    setScroll(Math.round(window.scrollY));
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const dispatch=useDispatch();

  const modalDeleteFriend=useSelector((state)=>state.profile.modalDeleteFriend.state);

  const modalDeleteFriendClose=()=>{
    dispatch(modalDeleteFriendState(false));
  };

  const deleteFriend=()=>{
    dispatch(modalDeleteFriendState(false));
  };

  return (
    <div className={modalDeleteFriend?style.modalWrapper:style.displayNone} style={{top:`${scroll}px`}}>
      <div className={style.modal}>
        <header className={style.modalHeader}>
          <h2 className={style.modalHeadertittle}>Remove Анна Надточий from friends</h2>
          <button className={style.modalHeaderBtn}>
            <Cross className={style.modalHeaderBtnImg} onClick={modalDeleteFriendClose}/>
          </button>
        </header>
        <main className={style.modalMain}>
          <p className={style.modalMainText}>Are you sure you want to remove Анна Надточий from your friends list?</p>
          <div className={style.modalMainBtnWrapper}>
            <button className={style.modalMainBtnFalse} onClick={modalDeleteFriendClose}>No</button>
            <button className={style.modalMainBtnTrue} onClick={deleteFriend}>Сonfirm</button>
          </div>
        </main>
      </div>
    </div>
  );
};
export default ModalDeleteFriend;