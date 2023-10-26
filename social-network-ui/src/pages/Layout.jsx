import {Outlet} from "react-router";
import Aside from "../components/Aside/Aside";
import Header from "../components/Header/Header";
import styles from "../components/Header/Header.module.scss"
import {useLocation} from "react-router-dom";
export default function Layout() {
    const location = useLocation();
    const excludedPaths = ['/messages', '/profile', '/login'];
    const showAside = !excludedPaths.some(path => location.pathname.startsWith(path));
    return (
        <div className={styles.container}>
            <Header/>
            {showAside && <Aside/>}
            <Outlet/>
        </div>
    )
}