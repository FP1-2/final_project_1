import "./index.scss"
export default function Avatar ({name, additionalClass}){
    return (
        <div className={additionalClass ? "avatar-wrapper "+additionalClass : "avatar-wrapper "}>
            <img src="/img/a.jpg" alt={name} />
        </div>

    )
}