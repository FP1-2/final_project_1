import axios from 'axios';

export const getPhotoURL = async (file) => {
    const formFIle = new FormData();
    formFIle.append("file", file);
    formFIle.append("upload_preset", "q8jkfqti");
    return (await axios.post("https://api.cloudinary.com/v1_1/ditpsafw3/image/upload", formFIle)).data.url;

}