import './style.scss'
import Avatar from "../Avatar/Avatar";
export default function ChatItem({message, photo, name, unread, read, additionalClass, additionalClass2}){
    return (
        <div className='chat-item-container'>
            <div  className='chat-item-container__photo'>
                <Avatar photo={photo} name={name} additionalClass={additionalClass}/>
            </div>
            <div  className='chat-item-container__info-wrap'>
                <div className='chat-item-container__info'>
                    <p className='chat-item-container__info__username'>{name}</p>
                    <p className={unread ? 'chat-item-container__info__last-message chat-item-container__info__last-message--unread' : 'chat-item-container__info__last-message'}>{message}</p>
                </div>
                <div className='chat-item-container__status'>
                    {unread && <div className='chat-item-container__status--unread'></div>}
                    {read && <div className='chat-item-container__status--read'>
                        <Avatar photo={photo} additionalClass={additionalClass2}/>
                    </div>}
                </div>
            </div>

        </div>
    )

}