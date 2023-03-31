# java-filmorate
Filmorate project.

---
## ER Диаграмма  
![](/ERD.png)  
## Описание  
***user*** - содержит данные о пользователях  

***film*** - содержит данные о фильмах  

***genre*** - содержит данные о жанрах  

***film_genre*** - соединительная таблица между фильмами и жанрами  
-- Позволяет присвоить одному фильму несколько жанров, а одному жанру несколько фильмов  

***user_like_film*** - содержит данные о лайках пользователя фильму (одна строка - один лайк)

***user_friend*** - содержит данные о друзьях пользователя  
-- Позволяет присвоить одному пользователю несколько друзей (пользователей из таблицы пользователи)

## Примеры запросов
```SQL  
SELECT * FROM film;  
-- Выгружаем все фильмы

SELECT * FROM film WHERE duration > 100;  
-- Выгружаем все фильмы c продолжительностью более 100 минут

SELECT g.name FROM genre g  
JOIN film_genre fg ON fg.genre_id=g.genre_id  
WHERE fg.film_id = 1;  
-- Выгружаем названия жанров для фильма с ID=1

SELECT f.* FROM film f
JOIN film_genre fg ON fg.film_id=f.film_id
JOIN genre g ON g.genre_id=fg.genre_id
WHERE g.name = 'Комедия';  
-- Выгружаем все фильмы с жанром "Комедия"

SELECT f.*,  
       COUNT(ulf.*) likes
FROM film f
JOIN user_like_film ulf ON ulf.film_id=f.film_id
GROUP BY f.film_id
ORDER BY likes DESC
LIMIT 10;
-- Выгружаем топ-10 фильмов по количеству лайков

SELECT f.* FROM film f
JOIN user_like_film ulf ON ulf.film_id=f.film_id
WHERE ulf.user_id = 1
-- Выгружаем все фильмы, которые лайкнул пользователь с ID=1

SELECT fr.name
FROM user u
JOIN user_friend uf ON uf.user_id=u.user_id
JOIN user fr ON fr.user_id=uf.friend_id
WHERE u.user_id = 1;
-- Выгружаем имена друзей пользователя с ID=1

SELECT fr.name
FROM user u
JOIN user_friend uf ON uf.user_id=u.user_id
JOIN user fr ON fr.user_id=uf.friend_id
WHERE u.user_id = 1
    AND fr.name IN (
            -- Подзапрос, в котором мы находим имена друзей пользователя с ID=2
            SELECT fr.name
            FROM user u
            JOIN user_friend uf ON uf.user_id=u.user_id
            JOIN user fr ON fr.user_id=uf.friend_id
            WHERE u.user_id = 2
)
-- Выгружаем имена общих друзей пользователя с ID=1 и пользователя с ID=2
```

