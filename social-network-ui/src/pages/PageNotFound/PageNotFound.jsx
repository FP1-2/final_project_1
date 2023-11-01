import ErrorPage from "../../components/ErrorPage/ErrorPage";
import styles from "./PageNotFound.module.scss";
export default function PageNotFound() {
  return (
    <ErrorPage message="Page Not Found" className={styles.notFoundPage} />
  );
}