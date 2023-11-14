import styles from './Loader.module.scss';

export default function Loader() {
  return (
    <div className={styles.loderWrapper}>
      <div className={styles.loder}></div>
    </div>
  );
}