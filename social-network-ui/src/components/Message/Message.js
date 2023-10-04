import "./style.scss"
import Avatar from "../Avatar/Avatar";
export default function Message({className, text, authUser, datetime, isHovered, photo}){
    return (
        <div className="chat-messageWrapper">
            <div className={authUser ? "chat-messageWrapper__cont authUser" : "chat-messageWrapper__cont chatUser"}>
                {!authUser && <div className="chat-messageWrapper__avatar">
                    <Avatar photo={photo} additionalClass="chat-messageWrapper__avatar"/>
                </div>}

                <div className={authUser ? "chat-messageWrapper__cont__text authUser" : "chat-messageWrapper__cont__text chatUser"}>
                    {isHovered && <div  className="chat-messageWrapper__cont__text__datatime">
                        <p>{datetime}</p>
                    </div>}

                    <p>{text}</p>

                </div>

            </div>
        </div>
    )
}