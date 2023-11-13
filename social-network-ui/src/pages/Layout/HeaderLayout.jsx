import {Outlet} from "react-router";
import Header from "../../components/Header/Header";
import styles from "./Layout.module.scss";
import React from "react";
import {useSelector} from "react-redux";

export default function HeaderLayout() {
  return (
    <React.Fragment>
      <Header authUser={useSelector(state => state.auth.user.obj)} 
        showMessageIcon={!location.pathname.startsWith('/messages')}/>
      <main className={styles.headerMain}>
        <Outlet/>
      </main>
    </React.Fragment>
  );
}
