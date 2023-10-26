import styles from './Loader.module.scss'

export default function Loader() {
    return (
        <div className={styles.loaderWheel}>
            <i> <i><i><i><i><i><i><i><i><i><i><i>
            </i></i></i></i></i></i></i></i></i></i></i></i>
        </div>
    )
}