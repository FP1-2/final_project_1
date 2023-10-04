import "./style.scss"
import ChatItem from "../ChatNavigation/ChatItem";
import Message from "./Message";
import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

export default function ChatField({chat, photo, name}){
    const chatTest = {
        id: 4,
        chatParticipant: {
            name: "Kate",
            surname: "Koval",
            username: "kateKoval",
            avatar: "",
        }
    }
    const isHovered = false;
    const authUserTest ={
        id: 99,
        name: "Olha",
        surname: "Khomych",
        username: "olhaKhomych",
        avatar: "",
    }
    const messagesTest = [
        {
            id:1,
            text: "Hello",
            sender: {
                name: "Olha",
                surname: "Khomych",
                username: "olhaKhomych",
                avatar: "",
            },
            createdDate: "2 жовтня 2023р., 22:25",
            status: "READ",
            chat_id: 2
        },
        {
            id:2,
            text: "Hi!How are you",
            sender: {
                name: "Ted",
                surname: "Crus",
                username: "tedCrus",
                avatar: "",
            },
            createdDate: "2 жовтня 2023р., 22:27",
            status: "SENT",
            chat_id: 2
        }
    ]
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState ([]);
    const [message, setMessage] = useState("");
    const [username, setUsername] = useState('');
    const token = localStorage.getItem('token');
    useEffect(() => {
        const socket = new SockJS('http:/localhost:9000/ws');
        const client = Stomp.over(socket);

        const headers = {
            Authorization: `Bearer ${token}`,
        }

        client.connect(headers, () => {
            client.subscribe('/user/messages'+chatId, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMess) => [...prevMess, receivedMessage]);
            })
        })

        setStompClient(client)


        return () => {
            client.disconnect();
        }
    }, []);

    const handleMessageChange = (e) => {
        setMessage(e.target.value)
    }
    const sendMessage = () =>{
        const chatMessage = {
            chatId: 1,
            text: message
        }

        stompClient.send('/app/chat', {}, JSON.stringify(chatMessage));
        sendMessage('');
    }


    return (
        <section className="chat-field-section">
            {chat === null ?
                <div className="chat-field-section__empty">
                    <h1 >Виберіть чат або почніть нову переписку</h1>
                </div>
               :
                <div className="chat-field-section__chat">
                    <div className="chat-field-section__chat__header">
                        <ChatItem
                            photo={chatTest.chatParticipant.avatar}
                            name={chatTest.chatParticipant.name+" "+chatTest.chatParticipant.surname }
                            additionalClass="chat-field-section__chat__header__card"/>
                    </div>
                    <div className="chat-field-section__chat__body">
                        <ul className="chat-field-section__chat__body__mess-list">
                            {messagesTest.map(({_id, text,createdDate,sender})=>{
                                return (<li key={_id} className="chat-field-section__chat__body__mess-list__items__item">
                                    <Message
                                        text={text}
                                        authUser={authUserTest.username === sender.username}
                                        datetime={createdDate}
                                        isHovered={!isHovered}
                                        photo={sender.avatar}
                                        additionalClass=""
                                    />
                                </li>)
                            })}
                        </ul>
                    </div>
                    <div className="chat-field-section__chat__input">
                        <form action="POST">
                            <input type="text" placeholder="Aa"/>
                        </form>
                    </div>
                </div>

            }

        </section>
    )
}