import React from 'react';
import { Route, Routes } from 'react-router-dom';
import LoginPage from "./pages/LoginPage/LoginPage";
import FavoritesPage from './pages/FavoritesPage/FavoritesPage';
import MessagesPage from './pages/MessagesPage/MessagesPage';
import NotificationsPage from './pages/NotificationsPage/NotificationsPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import RegistrationForm from "./components/RegistrationForm/RegistrationForm";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import AllFriends from './components/AllFriends/AllFriends';
import IncomingFriendRequests from './components/IncomingFriendRequests/IncomingFriendRequests';
import OutgoingFriendRequests from './components/OutgoingFriendRequests/OutgoingFriendRequests';
import SearchFriend from './components/SearchFriend/SearchFriend';
import PostsPageProfile from "./components/PostsPageProfile/PostsPageProfile";
import FriendsPage from './pages/FriendsPage/FriendsPage';
import FriendPageProfile from './components/FriendPageProfile/FriendPageProfile';
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";
import Layout from "./pages/Layout/Layout";
import PropTypes from "prop-types";
import Chat from "./components/Chat/Chat";
import PageNotFound from './pages/PageNotFound/PageNotFound';
import PopupMessage from "./components/PopupMessage/PopupMessage";
import MainPage from "./pages/MainPage/MainPage";
import PostPage from "./pages/PostPage/PostPage";
import GroupPage from "./pages/GroupPage/GroupPage";
import ResetPassword from './pages/ChangePasswordForm/ResetPassword';
import UpdatePass from './pages/ChangePasswordForm/UpdatePassword';
import GroupsPage from "./pages/GroupsPage/GroupsPage";
import GroupsPageProfile from "./components/GroupsPageProfile/GroupsPageProfile";

function AppRoutes({ isAuth }) {
  return (
    <React.Fragment>
      <PopupMessage />
      <Routes>
        <Route path="/" element={<ProtectedRoute isAuth={isAuth} content={<Layout />} />}>
          <Route index element={<ProtectedRoute isAuth={isAuth} content={<MainPage />} />} />
          <Route exact path='/profile/:id/*' element={<ProfilePage />}>
            <Route path='' element={<PostsPageProfile />} />
            <Route path='friends' element={<ProtectedRoute isAuth={isAuth} content={<FriendPageProfile />} />} />
            <Route path='groups' element={<ProtectedRoute isAuth={isAuth} content={<GroupsPageProfile />} />} />
          </Route>
          <Route path="/post/:id" element={<ProtectedRoute isAuth={isAuth} content={<PostPage />} />} />
          <Route path="/groups/:id" element={<ProtectedRoute isAuth={isAuth} content={<GroupPage />} />} />
          <Route path="/messages" element={<ProtectedRoute isAuth={isAuth} content={<MessagesPage />} />}>
            <Route path="/messages/:chatId" element={<ProtectedRoute isAuth={isAuth} content={<Chat />} />} />
          </Route>
          <Route path="/groups" element={<ProtectedRoute isAuth={isAuth} content={<GroupsPage />} />} />
          <Route path="/favorites" element={<ProtectedRoute isAuth={isAuth} content={<FavoritesPage />} />} />
          <Route path="/notifications" element={<ProtectedRoute isAuth={isAuth} content={<NotificationsPage />} />} />
          <Route path='/friends/*' element={<ProtectedRoute isAuth={isAuth} content={<FriendsPage />}/> }>
            <Route path='' element={<ProtectedRoute isAuth={isAuth} content={<AllFriends />}></ProtectedRoute> }></Route>
            <Route path='incoming-requests' element={<ProtectedRoute isAuth={isAuth} content={<IncomingFriendRequests />}></ProtectedRoute> }></Route>
            <Route path='outgoing-requests' element={<ProtectedRoute isAuth={isAuth} content={<OutgoingFriendRequests />}></ProtectedRoute> }></Route>
            <Route path='search-friend' element={<ProtectedRoute isAuth={isAuth} content={<SearchFriend />}></ProtectedRoute> }></Route>
          </Route>
          
          <Route path={'*'} element={<PageNotFound />} />
        </Route>
        <Route path="/login" element={<LoginPage isAuth={isAuth} />} />
        <Route path='/registration' element={<RegistrationForm />} />
        <Route path='/registration/confirm' element={<ConfirmRegistration />} />
        <Route path='/reset-password' element={<ResetPassword/>}/>
        <Route path='/change_password/:token' element={<UpdatePass/>}/>
      </Routes>
    </React.Fragment>
  );
}

export default AppRoutes;

AppRoutes.propTypes = {
  isAuth: PropTypes.bool.isRequired
};