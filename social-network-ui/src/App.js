import RegistrationForm from "./components/RegistrationForm/RegistrationForm"
import { Route, Routes } from "react-router-dom";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import Profile from "./components/Profile/Profile";
import Post from "./components/Post/Post";
import FriendPageProfile from "./components/FriendPageProfile/FriendPageProfile";
import PostsPageProfile from "./components/PostsPageProfile/PostsPageProfile";
import LikedPageProfile from "./components/LikedPageProfile/LikedPageProfile";

function App() {
  return (
    <Routes>
      <Route path='/registration' element={<RegistrationForm />} />
      <Route path='/' element={
        <div style={{ border: "1px solid #ccc", padding: "20px", width: "200px" }}>
          <h2>Тест</h2>
          <p>Тестуємо головний роут</p>
        </div>
      } />
      <Route path='/registration/confirm' element={<ConfirmRegistration />} />
      <Route path='/post' element={<Post />} />
      <Route path='/profile/*' element={<Profile />}>
        <Route path='' element={<PostsPageProfile />} />
        <Route path='friends' element={<FriendPageProfile />} />
        <Route path='liked' element={<LikedPageProfile/>} />
      </Route>

    </Routes>
  );
}

export default App;
