CREATE TABLE IF NOT EXISTS mpa (
	mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(5) NOT NULL
);
CREATE TABLE IF NOT EXISTS film (
	film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	title varchar(50) NOT NULL,
	description varchar(200),
	release_date date,
	duration int,
	mpa_id int REFERENCES mpa ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS genre (
	genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS film_genre (
	film_id int REFERENCES film ON DELETE CASCADE,
	genre_id int REFERENCES genre ON DELETE CASCADE,
	PRIMARY KEY (film_id, genre_id)
);
CREATE TABLE IF NOT EXISTS director (
    director_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(30) NOT NULL
);
CREATE TABLE IF NOT EXISTS film_director (
	film_id int REFERENCES film ON DELETE CASCADE,
	director_id int REFERENCES director ON DELETE CASCADE,
	PRIMARY KEY (film_id, director_id)
);
CREATE TABLE IF NOT EXISTS users (
	user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	login varchar(20) NOT NULL,
	name varchar(30) NOT NULL,
	email varchar(30),
	birthday date
);
CREATE TABLE IF NOT EXISTS user_friend (
	user_id int REFERENCES users ON DELETE CASCADE,
	friend_id int REFERENCES users ON DELETE CASCADE,
	is_accepted boolean NOT NULL,
	PRIMARY KEY (user_id, friend_id)
);
CREATE TABLE IF NOT EXISTS user_like_film (
	film_id int REFERENCES film ON DELETE CASCADE,
	user_id int REFERENCES users ON DELETE CASCADE,
	PRIMARY KEY (film_id, user_id)
);
CREATE TABLE IF NOT EXISTS feed (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamp TIMESTAMP,
    user_id int REFERENCES users ON DELETE CASCADE,
    event_type varchar(24),
    operation varchar(24),
    event_id INTEGER,
    entity_id INTEGER
)