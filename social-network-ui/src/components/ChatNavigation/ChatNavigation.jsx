import "./style.scss"
import ChatItem from "./ChatItem";
import Search from "../icons/Search";

export default function ChatNavigation ({chats}){

    const authUser ={
            id: 99,
            name: "Olha",
            surname: "Khomych",
            username: "olhaKhomych",
            avatar: "",
        }
    // test chats list
    const chat = [
        {
            id: 1,
            chatParticipant: {
                    name: "Alex",
                    surname: "Smith",
                    username: "alexSmith",
                    avatar: "",
                },
            lastMessage: null
        },
        {
            id: 2,
            chatParticipant: {
                    name: "Ted",
                    surname: "Crus",
                    username: "tedCrus",
                    avatar: "",
                },
            lastMessage: {
                id:1,
                text: "Hello",
                sender: {
                    name: "Olha",
                    surname: "Khomych",
                    username: "olhaKhomych",
                    avatar: "",
                },
                createdDate: "",
                status: "READ",
            }
        },
        {
            id: 3,
            chatParticipant: {
                name: "Ron",
                surname: "Whisly",
                username: "ronWhisly",
                avatar: "",
            },
            lastMessage: {
                id:1,
                text: "Hello",
                sender: {
                    name: "Olha",
                    surname: "Khomych",
                    username: "olhaKhomych",
                    avatar: "",
                },
                createdDate: "",
                status: "SENT",
            }
        },
        {
            id: 4,
            chatParticipant: {
                name: "Kate",
                surname: "Koval",
                username: "kateKoval",
                avatar: "",
            },
            lastMessage: {
                id:1,
                text: "Hi! How Are You?",
                sender: {
                    name: "Kate",
                    surname: "Koval",
                    username: "kateKoval",
                    avatar: "",
                },
                createdDate: "",
                status: "SENT",
            }
        },
        {
            id: 6,
            chatParticipant: {
                name: "Donna",
                surname: "Amigo",
                username: "donnaAmigo",
                avatar: "",
            },
            lastMessage: {
                id:1,
                text: "Nice to see you. I'm sure it will be great vacation! Bye, bye and good bye!!!!",
                sender: {
                    name: "Donna",
                    surname: "Amigo",
                    username: "donnaAmigo",
                    avatar: "",
                },
                createdDate: "",
                status: "SENT",
            }
        }
    ]
    return (
        <section className="chat-nav-section">
            <div className="chat-nav-section__header">
                <div className="chat-nav-section__header__text">
                    <h1>Чати</h1>
                </div>
                <div className="chat-nav-section__header__button-wrapper">
                    <div />
                </div>
            </div>
            <div className="chat-nav-section__search">
                <div className="chat-nav-section__search__input">
                    <span>
                        <Search/>
                    </span>
                    <input type="text" placeholder="Поиск в Messenger"/>
                </div>
            </div>
            <div className="chat-nav-section__chat-list">
                <ul className="chat-nav-section__chat-list__items">
                    {chats.map(({_id, chatParticipant: chatParticipant, lastMessage})=>{
                        return (<li key={_id} className="chat-nav-section__chat-list__items__item">
                            <ChatItem
                                additionalClass="chat-nav-section__chat-list__items__item--avatar"
                                additionalClass2="chat-item-container__status--read"
                                name={chatParticipant.name+" "+chatParticipant.surname}
                                photo={chatParticipant.photo}
                                message={ lastMessage === null ? ""
                                : (lastMessage.sender.username === authUser.username ? ("Ви: "  + lastMessage.text) : lastMessage.text)}
                                unread={ lastMessage !== null && lastMessage.sender.username !== authUser.username && lastMessage.status === "SENT"}
                                read={ lastMessage !== null && lastMessage.sender.username === authUser.username && lastMessage.status === "READ"}
                            />
                        </li>)
                    })}
                </ul>
            </div>
        </section>
    )
}
