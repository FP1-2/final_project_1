import './style.scss'
import ChatNavigation from "../../components/ChatNavigation/ChatNavigation";
import ChatField from "../../components/Message/ChatField";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {createChat, loadChat, loadChats} from "../../redux-toolkit/messenger/asyncThunk";
export default function Messenger (){
    const dispatch = useDispatch();
    const {chats, chat} = useSelector((state) => ({
        chats: state.chats.obj,
        chat: state.chat.obj,
    }));
    const [chatId, setChatId] = useState(null);
    const [username, setUsername] = useState('');

    useEffect(() => {
        dispatch(loadChats({ page: 0, size: 10 }));
    }, [dispatch]);
    const handleLoadChat = () => {
        dispatch(loadChat(chatId));
    };
    const handleCreateChat = () => {
        dispatch(createChat(username));
    };
    const handleDeleteChat = () => {
        dispatch(createChat(chatId));
    };

    return (
        <div className="messenger-page">
            <ChatNavigation
                chats={chats}/>
            <ChatField/>
        </div>
    )
}
