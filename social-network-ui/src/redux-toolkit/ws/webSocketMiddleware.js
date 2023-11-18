import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {setNewMessage, setMessageWithNewStatus, setIsVisible, setNotificationQt} from "./slice";
import {setUnreadMessagesQt} from "../messenger/slice";
const baseURL = process.env.REACT_APP_BASE_URL;
let client = null;

const webSocketMiddleware = (store) => (next) => (action) => {
  const authUser = store.getState().auth?.user?.obj;
  const token = store.getState().auth?.token?.obj.token;
  const headers = {Authorization: `Bearer ${token}`};
  if (!authUser || !token) {
    return next(action);
  }
  
  switch (action.type) {
    case 'webSocket/connect': {
      const socket = new SockJS(`${baseURL}/ws`);
      client = Stomp.over(() => socket);
      client.connect(
        headers,
        () => {
          const queuePath = `/user/${authUser.username}/queue`;
          const subscribeHandler = (queue, actionCreator) => (message) => {
            const messageResponse = JSON.parse(message.body);
            store.dispatch(actionCreator(messageResponse));
          };
          store.dispatch(setIsVisible(false));
          client.subscribe(`${queuePath}/messages`, subscribeHandler('/queue/messages', setNewMessage), headers);
          client.subscribe(`${queuePath}/messageStatus`, subscribeHandler('/queue/messageStatus', setMessageWithNewStatus), headers);
          client.subscribe(`${queuePath}/messageNotification`, subscribeHandler('/queue/messageNotification', setUnreadMessagesQt), headers);
          client.subscribe(`${queuePath}/notification`, subscribeHandler('/queue/notification', setNotificationQt), headers);
        },
        (error) => {
          console.log('Connection error:', error);
          store.dispatch(setIsVisible(true));
        },
        () => {
          console.log('Connection close');
          store.dispatch(setIsVisible(true));
        }
      );
      return () => {
        if (client && client.connected) {
          store.dispatch(setIsVisible(true));
          client.disconnect();
        }
      };
    }
    case 'webSocket/sendMessage' : {
      const message = action.payload;
      try{
        client.send('/app/chat', headers, JSON.stringify(message));
      } catch(error){
        console.error("Cannot connect to websocket.")
        try{
          console.log("Trying to reconnect.")
          store.dispatch({type: 'webSocket/connect'});
          client.send('/app/chat', headers, JSON.stringify(message));
        } catch(error){
          console.error("Something wrong with server/websocket connection. Please restar your app.")
        }
      }
      break;
    }
    case 'webSocket/updateMessageStatus': {
      const messageId = action.payload;
      try{
        client.send(`/app/updateMessageStatus/${messageId}`, headers, JSON.stringify('READ'));
      } catch(error){
        console.error("Cannot connect to websocket.")
        try{
          console.log("Trying to reconnect.")
          store.dispatch({type: 'webSocket/connect'});
          client.send(`/app/updateMessageStatus/${messageId}`, headers, JSON.stringify('READ'));
        } catch(error){
          console.error("Something wrong with server/websocket connection. Please restar page.")
        }
      }
      break;
    }
    case 'webSocket/close': {
      if (client && client.connected) {
        client.disconnect();
      }
      break;
    }
    default:
      return next(action);
  }
}

export default webSocketMiddleware;