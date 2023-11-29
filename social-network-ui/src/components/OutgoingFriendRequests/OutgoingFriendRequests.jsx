import style from './OutgoingFriendRequests.module.scss';
import { allRequests } from '../../redux-toolkit/friend/thunks';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
const OutgoingFriendRequests=()=>{
  const dispatch=useDispatch();
  useEffect(()=>{
    dispatch(allRequests());
  }
  ,[]);
  const myRequests=useSelector(state=>state.friends.allRequests.obj.send);
  return(
    <div className={style.s}>
      {myRequests.map((el)=>(
        el.name
      ))}
    </div>);
};
export default  OutgoingFriendRequests;