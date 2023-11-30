import style from './OutgoingFriendRequests.module.scss';
import { allRequests } from '../../redux-toolkit/friend/thunks';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import FriendRequestCard from '../FriendRequestCard/FriendRequestCard';
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
        <div key={el.id}>
          <FriendRequestCard type="SENT_REQUEST" friendRequest={el}></FriendRequestCard>
{/*           {el.name}
 */}        </div>
      ))}
    </div>);
};
export default  OutgoingFriendRequests;