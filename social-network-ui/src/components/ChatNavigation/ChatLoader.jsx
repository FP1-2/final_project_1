import styles from "./ChatNavigation.module.scss";

const pendingItems = new Array(12).fill(null);
const ChatLoader = () => {
  return (
    <>
      <div className={styles.chatNavSection__header}>
        <div className={styles.chatNavSection__header__text}>
          <span></span>
        </div>
        <div className={styles.chatNavSection__header__buttonWrapper}>

        </div>
      </div>
      <div className={styles.chatNavSection__search}>
        <div className={styles.chatNavSection__search__input}>
                <span>
                </span>
          <input type="text"/>
        </div>
      </div>
      <div className={styles.chatNavSection__chatList}>
        <ul className={`${styles.chatNavSection__chatList__items} ${styles.pending}`}>
          {pendingItems.map((_, index) => (
            <li key={index} className={`${styles.chatNavSection__chatList__items__item} ${styles.pending__item}`}>
              <div className={`${styles.chatNavSection__chatList__items__item} ${styles.pending__item__avatar}`}></div>
              <div className={`${styles.chatNavSection__chatList__items__item} ${styles.pending__item__lines}`}>
                <div
                  className={`${styles.chatNavSection__chatList__items__item} ${styles.pending__item__lines__line1}`}></div>
                <div
                  className={`${styles.chatNavSection__chatList__items__item} ${styles.pending__item__lines__line2}`}></div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </>
  );
};

export default ChatLoader;