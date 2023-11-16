import React from "react";
import style from "./ModalDeleteFriend.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import {useDispatch,useSelector} from "react-redux";
import { modalDeleteFriendState} from "../../redux-toolkit/friend/slice";
import { deleteMyFriend } from "../../redux-toolkit/friend/thunks";
import { deleteLocalFriend } from "../../redux-toolkit/friend/slice";

const ModalDeleteFriend = () => {

  const dispatch=useDispatch();

  const modalDeleteFriend = useSelector((state) => state.friends.modalDeleteFriend);
  const el = useSelector((state) => state.friends.friend.obj);

  const modalDeleteFriendClose=()=>{
    dispatch(modalDeleteFriendState(false));
  };

  const deleteFriend=()=>{
    dispatch(deleteMyFriend({friendId:el.id}));
    dispatch(deleteLocalFriend(el.id));
    dispatch(modalDeleteFriendState(false));
  };

  return (
    <div className={modalDeleteFriend?style.modalWrapper:style.displayNone} style={{top:`${scroll}px`}}>
      <div className={style.modal}>
        <header className={style.modalHeader}>
          <h2 className={style.modalHeadertittle}>Remove {`${el.name} ${el.surname}`} from friends</h2>
          <button className={style.modalHeaderBtn}>
            <Cross className={style.modalHeaderBtnImg} onClick={modalDeleteFriendClose}/>
          </button>
        </header>
        <main className={style.modalMain}>
          <p className={style.modalMainText}>Are you sure you want to remove {`${el.name} ${el.surname}`} from your friends list?</p>
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