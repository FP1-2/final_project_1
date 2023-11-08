import React, {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {loadNotifications} from '../../redux-toolkit/notification/thunks';
import {resetNotificationsState} from '../../redux-toolkit/notification/slice';
import Notification from '../../components/Notification/Notification';
import styles from './NotificationsPage.module.scss';
import {createHandleScroll} from "../../utils/utils";

export default function NotificationsPage() {
  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();

  const {
    status,
    obj: { 
      content,
      totalPages, 
      pageable: { 
        pageNumber 
      }
    } 
  } = useSelector(state => state.notifications.notifications);

  useEffect(() => {
    dispatch(resetNotificationsState());
    dispatch(loadNotifications({ page: 0 }));
  }, []);

  const getMoreNotifications = () => {
    if (status !== 'pending' && pageNumber < totalPages) {
      dispatch(loadNotifications({ page: pageNumber + 1 }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore:  getMoreNotifications,
  });

  return (
    <div className={styles.container} onScroll={handleScroll} ref={scrollContainerRef}>
      <div>
        {content.map((notification) => (
          <Notification key={notification.id} notification={notification} />
        ))}
      </div>
    </div>
  );
}
