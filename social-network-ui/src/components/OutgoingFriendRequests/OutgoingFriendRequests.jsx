import style from './OutgoingFriendRequests.module.scss';
import { allRequests } from '../../redux-toolkit/friend/thunks';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import FriendRequestCard from '../FriendRequestCard/FriendRequestCard';
import EmptyMessage from '../EmptyMessage/EmptyMessage';
const OutgoingFriendRequests=()=>{
  const dispatch=useDispatch();
  useEffect(()=>{
    dispatch(allRequests());
  }
  ,[]);
  const myRequests=useSelector(state=>state.friends.allRequests.obj.send);
  return(
    <div className={style.s}>
      {myRequests.length > 0 ? (
        myRequests.map(el => (
          <div key={el.id}>
            <FriendRequestCard type="SENT_REQUEST" friendRequest={el} />
          </div>
        ))
      ) : (
        <EmptyMessage message="No outgoing friend requests." /> 
      )}
    </div>
  );
};
export default  OutgoingFriendRequests;