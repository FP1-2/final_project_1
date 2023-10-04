import {Stomp} from "@stomp/stompjs";
import SockJS from 'sockjs-client';
export const subscribeToMessages = (chatId) => (dispatch) => {
    const socket = new SockJS('http://localhost:9000/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        stompClient.subscribe(`/user/queue/chat/${chatId}`, (message) => {
            const messageData = JSON.parse(message.body);
            dispatch(receiveMessage(messageData));
        });
    });
};

export const receiveMessage = (message) => ({
    type: 'messages/receiveMessage',
    payload: message,
});