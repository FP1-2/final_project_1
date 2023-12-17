import React, {useEffect} from "react";
import style from "./ModalDeleteFriend.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import {useDispatch,useSelector} from "react-redux";
import { modalDeleteFriendState} from "../../redux-toolkit/friend/slice";
import { deleteMyFriend,getFriends } from "../../redux-toolkit/friend/thunks";
import { deleteLocalFriend } from "../../redux-toolkit/friend/slice";

const ModalDeleteFriend = () => {

  const dispatch=useDispatch();

  const modalDeleteFriend = useSelector((state) => state.friends.modalDeleteFriend);
  const el = useSelector((state) => state.friends.friend.obj);
  const myId = useSelector(state => state.auth.user.obj.id);

  const modalDeleteFriendClose=async ()=>{
    await dispatch(modalDeleteFriendState(false));
  };
  useEffect(() => {
    return ()=>{
      dispatch(modalDeleteFriendState(false));
    };
  }, []);
  const deleteFriend=async ()=>{
    await dispatch(deleteMyFriend({friendId:el.id}));
    await dispatch(deleteLocalFriend(el.id));
    await dispatch(modalDeleteFriendState(false));
    await dispatch(getFriends(myId));
  };

  return (
    <div className={modalDeleteFriend?style.modalWrapper:style.displayNone} style={{top:`${scroll}px`}}>
      <div className={style.modal}>
        <header className={style.modalHeader}>
          <h2 className={style.modalHeaderTitle}>Remove {`${el.name} ${el.surname}`} from friends</h2>
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