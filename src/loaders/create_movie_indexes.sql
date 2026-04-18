ALTER TABLE movies
    CREATE INDEX index_movie_titles
        ON movies(title);