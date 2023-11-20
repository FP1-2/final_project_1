import styles from './Chat.module.scss';
import {useState, useRef, useEffect} from "react";
import AddFile from "../Icons/AddFile";
import Send from "../Icons/Send";
import Like from "../Icons/Like";
import Emoji from "../Icons/Emoji";
import PropTypes from 'prop-types';
import {ContentType} from "../../utils/contentType";
import data from '@emoji-mart/data';
import Picker from '@emoji-mart/react';
import { getPhotoURL } from "../../utils/thunks";

export default function MessageInput({sendMessage, handleMessageChange, message, emojiHandler, handleMessageClick}) {
  const [isOpen, setIsOpen] = useState(false);
  const [uploadedFile, setUploadedFile] = useState(null);
  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    setUploadedFile(file);
  };
  const handleDeleteFile = () => {
    setUploadedFile(null);
    const fileInput = document.getElementById('fileInput');
    fileInput.value = '';
  };

  const handleSend = async () => {
    if (uploadedFile && message.trim() !== '') {
      await sendImage(uploadedFile);
      sendMessage(ContentType.TEXT, message);
    } else if (uploadedFile) {
      await sendImage(uploadedFile);
    } else if (message.trim() !== '') {
      sendMessage(ContentType.TEXT, message);
    }
  };
  async function sendImage(file){
    try {
      const photoURL = await getPhotoURL(file);
      sendMessage(ContentType.IMAGE, photoURL.data.url);
      handleDeleteFile();
    } catch (error) {
      alert('Сталася помилка під час завантаження, спробуйте ще раз.');
    }
    
  }

  function sendLike() {
    sendMessage("LIKE", "LikeBtn");
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSend();
    }
  };
  const handleClickOutside = (e) => {
    const searchUserElement = document.getElementById("emoji-picker");
    const btn = document.getElementById("emoji-btn");
    if (searchUserElement && !searchUserElement.contains(e.target) && !btn.contains(e.target)) {
      setIsOpen(false);
    }
  };
  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);
  
  return (
    <div className={styles.chat__input}>
      <FileInput handleFileUpload={handleFileUpload}/>
      <div className={styles.chat__input__area}>
        <div className={styles.chat__input__area__wrap}>

          {uploadedFile && <UploadedFile uploadedFile={uploadedFile} handleDeleteFile={handleDeleteFile}/>}

          <TextInput
            handleMessageChange={handleMessageChange}
            handleMessageClick={handleMessageClick}
            handleKeyPress={handleKeyPress}
            message={message}
          />
        </div>

        <Emoji emojiClickHandler={() => setIsOpen(!isOpen)}/>
      </div>

      <Like size={"20"} onClick={sendLike} styleClass={styles.likeBtn}></Like>

      <Send sendMessage={handleSend} isVisible={(message.trim() !== '') || !!uploadedFile}/>
      <EmojiPicker isOpen={isOpen} setIsOpen={setIsOpen} emojiHandler={emojiHandler}/>

    </div>
  );

}

MessageInput.propTypes = {
  sendMessage: PropTypes.func.isRequired,
  handleMessageClick: PropTypes.func.isRequired,
  handleMessageChange: PropTypes.func.isRequired,
  message: PropTypes.string.isRequired,
  emojiHandler: PropTypes.func.isRequired,
};

function FileInput({handleFileUpload}) {
  const fileInputRef = useRef();

  const handleIconClick = () => {
    fileInputRef.current.click();
  };

  return (
    <>
      <label htmlFor="fileInput">
        <AddFile type="file" onClick={handleIconClick}/>
      </label>
      <input
        type="file"
        accept="image/*"
        id="fileInput"
        style={{display: 'none'}}
        onChange={handleFileUpload}
      />
    </>
  );
}

FileInput.propTypes = {
  handleFileUpload: PropTypes.func.isRequired,
};

function UploadedFile({uploadedFile, handleDeleteFile}) {
  return (
    <div className={styles.chat__input__area__file}>
      <img src={URL.createObjectURL(uploadedFile)} alt={uploadedFile.name} width="48px" height="48px"/>
      <div className={styles.chat__input__area__file__closeBtn} onClick={handleDeleteFile}>&#10006;</div>
    </div>
  );
}

UploadedFile.propTypes = {
  uploadedFile: PropTypes.object,
  handleDeleteFile: PropTypes.func.isRequired,
};

function TextInput({handleMessageChange, handleKeyPress, message, handleMessageClick}) {
  return (
    <textarea
      placeholder="Aa"
      onClick={handleMessageClick}
      onChange={handleMessageChange}
      onKeyDown={handleKeyPress}
      value={message}
    />
  );
}

TextInput.propTypes = {
  handleMessageClick: PropTypes.func.isRequired,
  handleMessageChange: PropTypes.func.isRequired,
  handleKeyPress: PropTypes.func.isRequired,
  message: PropTypes.string.isRequired,
};

function EmojiPicker({isOpen, setIsOpen, emojiHandler}) {
  
  return (
    isOpen &&
    <div className={styles.pickerWrapper} id="emoji-picker" onClick={(e)=> e.preventDefault()}>
      <Picker
        data={data}
        theme="light"
        navPosition="bottom"
        previewPosition='none'
        skinTonePosition='none'
        perLine={8}
        maxFrequentRows={0}
        emojiSize={30}
        icons="outline"
        onEmojiSelect={(emoji) => {
          emojiHandler(emoji);
          setIsOpen(false);
        }}
      />
    </div>
  );
}

EmojiPicker.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  setIsOpen: PropTypes.func.isRequired,
  emojiHandler: PropTypes.func.isRequired,
};