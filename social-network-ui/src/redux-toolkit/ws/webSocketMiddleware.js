import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import {setNewMessage, setMessageWithNewStatus, setUnreadMessages } from "./slice";
import { setUnreadMessagesQt } from "../messenger/slice";

let client = null;

const webSocketMiddleware = (store) => (next) => (action) => {
  const username = store.getState().messenger.user.obj.username;
  const token = localStorage.getItem('authToken');
  const headers = { Authorization: `Bearer ${token}` };

  switch (action.type){
    case 'webSocket/connect': {
      const socket = new SockJS('http://localhost:9000/ws');
      client = Stomp.over(()=>socket);
      client.connect(
        headers,
        () => {
          const queuePath = `/user/${username}/queue`;
          const subscribeHandler = (queue, actionCreator) => (message) => {
            const messageResponse = JSON.parse(message.body);
            store.dispatch(actionCreator(messageResponse));
          };

          client.subscribe(`${queuePath}/messages`, subscribeHandler('/queue/messages', setNewMessage), headers);
          client.subscribe(`${queuePath}/messageStatus`, subscribeHandler('/queue/messageStatus', setMessageWithNewStatus), headers);
          client.subscribe(`${queuePath}/notifications`, subscribeHandler('/queue/notifications', setUnreadMessagesQt), headers);
        },
        (error) => {
          console.log('Connection error:', error);
        }
      );
      return ()=>{
        if (client) {
          client.disconnect();
        }
      };
    }
    case 'webSocket/sendMessage' : {
      if(client){
        const message = action.payload;
        client.send('/app/chat', headers, JSON.stringify(message));
      }
      break;
    }
    case 'webSocket/updateMessageStatus': {
      if(client){
        const messageId = action.payload;
        client.send(`/app/updateMessageStatus/${messageId}`, headers, JSON.stringify('READ'));
      }
      break;
    }
    default:
      return next(action); 
  }
}
export default webSocketMiddleware;