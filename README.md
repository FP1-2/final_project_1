# Загальна інформація

Цей проект створений як аналог соціальній мережі Facebook, в якому реальзовані сторінка реєстрації і логіну, стрічка новин, сторінка користувача, сторінка повідомлень а також сторінка обраного.

# Технології
Проект був створений за допомогою таких технологій:
React,Redux Toolkit, Git, ESLint, Stylelint, HTML, CSS, SASS, npm, formik[social-network-ui](social-network-ui)

## Back-end:


# Контролер постів

Цей контролер надає засоби для обробки запитів, пов'язаних з постами. Користувачі можуть додавати коментарі, лайки, репости, а також отримувати та створювати пости.

## Кінцеві точки API:

### 1. Створення нового поста

- *URL*: `/api/posts/post`
- *Метод*: `POST`

*Приклад*: `/api/posts/post` 

- *Тіло запиту*:

*не обов'язкове поле* `"imageUrl"`

```json
{
  "imageUrl": "https://some-image-url.com/image.jpg",
  "title": "Це мій новий пост",
  "body": "Тут міститься вміст мого поста."
}
```

### 2. Створення репоста поста

- *URL*: `/api/posts/repost`
- *Метод*: `POST`

*Приклад*: `/api/posts/repost`

- *Тіло запиту*:

*обов'язкове поле* `"originalPostId"`

```json
{
  "imageUrl": "https://example.com/image.jpg",
  "title": "My Repost Title",
  "body": "This is the body of my repost.",
  "originalPostId": 12345
}
```

*Якщо `originalPostId` виявиться репостом, то буде репоститься оригінал цього репоста. Повторний запит з тим самим `originalPostId` видаляє репост та пов'язані з ним лайки та коментарі.*

### 3. Часткове оновлення існуючого поста/репоста

- *URL*: `/api/posts/update/{postId}`
- *Метод*: `PATCH`
- *Параметри*: `postId` - ідентифікатор поста/репоста

*Приклад*: `/api/posts/update/4`

- *Тіло запиту*:

*Будь-яке поле необов'язкове*

```json
{
  "imageUrl": "https://example.com/image.jpg",
  "title": "My Repost Title",
  "body": "This is the body of my repost."
}
```

### 4. Отримання постів користувача за його ID

- *URL*: `/api/posts/by_user_id/{userId}`
- *Метод*: `GET`
- *Параметри*:
    - `userId` - ідентифікатор користувача
    - `page` - номер сторінки (за замовчуванням: 0)
    - `size` - розмір сторінки (за замовчуванням: 10)
    - `sort` - параметри сортування (наприклад, "createdDate,desc")

*Приклад*: `/api/posts/by_user_id/1?page=0&size=10&sort=id,desc`

### 5. Отримання деталей поста за його ID

- *URL*: `/api/posts/{postId}`
- *Метод*: `GET`
- *Параметри*: `postId` - ідентифікатор поста/репоста

*Приклад*: `/api/posts/4`

### 6. Додавання коментаря до поста

- *URL*: `/api/posts/comment`
- *Метод*: `POST`

*Приклад*: `/api/posts/comment`
- *Тіло запиту*:

```json
{
  "postId": 4,
  "content": "Great post!"
}
```

### 7. Отримання коментарів для заданого поста

- *URL*: `/api/posts/{postId}/comments`
- *Метод*: `GET`
- *Параметри*:
  - `postId` - ідентифікатор поста/репоста
  - `page` - номер сторінки (за замовчуванням: 0)
  - `size` - розмір сторінки (за замовчуванням: 10)
  - `sort` - параметри сортування (наприклад, "createdDate,desc")

*Приклад*: `/api/posts/4/comments?page=0&size=10&sort=createdDate,desc`

### 8. Додавання "лайка" до поста

- *URL*: `/api/posts/like/{postId}`
- *Метод*: `POST`
- *Параметри*: `postId` - ідентифікатор поста/репоста

*Приклад*: `/api/posts/like/4`