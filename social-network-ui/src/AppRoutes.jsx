import React from 'react';
import { Route, Routes } from 'react-router-dom';
import LoginPage from "./pages/LoginPage/LoginPage";
import FavoritsPage from './pages/FavoritsPage/FavoritsPage';
import MessagesPage from './pages/MessagesPage/MessagesPage';
import NotificationsPage from './pages/NotificationsPage/NotificationsPage';
import PostsPage from './pages/PostsPage/PostsPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import RegistrationForm from "./components/RegistrationForm/RegistrationForm";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import PostsPageProfile from "./components/PostsPageProfile/PostsPageProfile";
import FriendPageProfile from "./components/FriendPageProfile/FriendPageProfile";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";
import Layout from "./pages/Layout/Layout";
import PropTypes from "prop-types";
import Chat from "./components/Chat/Chat";
import PageNotFound from './pages/PageNotFound/PageNotFound';
import FriendsPage from './pages/FriendsPage/FriendsPage';

function AppRoutes({ isAuth }) {
  return (
    <Routes>
      <Route path="/" element={<ProtectedRoute isAuth={isAuth} content={<Layout />} />}>
        <Route index element={<ProtectedRoute isAuth={isAuth} content={<PostsPage />} />} />
        <Route exact path='/profile/:id/*' element={<ProfilePage />}>
          <Route path='' element={<PostsPageProfile />} />
          <Route path='friends' element={<FriendPageProfile />} />
        </Route>
        <Route path="/messages" element={<ProtectedRoute isAuth={isAuth} content={<MessagesPage />} />}>
          <Route path="/messages/:chatId" element={<ProtectedRoute isAuth={isAuth} content={<Chat />} />} />
        </Route>
        <Route path="/favorites" element={<ProtectedRoute isAuth={isAuth} content={<FavoritsPage />} />} />
        <Route path="/notifications" element={<ProtectedRoute isAuth={isAuth} content={<NotificationsPage />} />} />
        <Route path={'*'} element={<PageNotFound />} />
      </Route>
      <Route path="/login" element={<LoginPage isAuth={isAuth} />} />
      <Route path="/friends" element={<FriendsPage/>}/>
      <Route path='/registration' element={<RegistrationForm />} />
      <Route path='/registration/confirm' element={<ConfirmRegistration />} />
    </Routes>
  );
}

export default AppRoutes;

AppRoutes.propTypes = {
  isAuth: PropTypes.bool.isRequired
};