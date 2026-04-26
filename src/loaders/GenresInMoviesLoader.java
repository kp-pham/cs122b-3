package loaders;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenresInMoviesLoader extends DataLoader {
    public GenresInMoviesLoader(Connection conn) {
        super(conn);
    }

    @Override
    protected void createStagingTable() throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS genres_in_movies_staging";
        String createQuery = "CREATE TABLE genres_in_movies_staging( " +
                             "    genreId TEXT, " +
                             "    movieId TEXT " +
                             ")";

        PreparedStatement statement = conn.prepareStatement(dropQuery);
        statement.executeUpdate();

        statement = conn.prepareStatement(createQuery);
        statement.executeUpdate();

        statement.close();
    }

    @Override
    protected void loadToStaging(String file) throws SQLException {
        String query = "LOAD DATA LOCAL INFILE ? " +
                       "INTO TABLE genres_in_movies_staging " +
                       "FIELDS TERMINATED BY ',' " +
                       "ENCLOSED BY '\"' " +
                       "LINES TERMINATED BY '\\r\\n' " +
                       "IGNORE 1 ROWS";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, file);

        statement.executeUpdate();
        statement.close();
    }

    @Override
    protected void validateAndTransform() throws SQLException {
        String query = "INSERT INTO genres_in_movies (genreId, movieId) " +
                       "WITH deduped AS ( " +
                       "    SELECT genreId, movieId " +
                       "    FROM genres_in_movies_staging " +
                       "    GROUP BY genreId, movieId " +
                       "    HAVING COUNT(*) = 1 " +
                       "), " +
                       "cleaned AS ( " +
                       "    SELECT CAST(S.genreId AS UNSIGNED) AS genreId, S.movieId " +
                       "    FROM genres_in_movies_staging AS S " +
                       "    INNER JOIN deduped AS D ON D.genreId = S.genreId " +
                       "                           AND D.movieId = S.movieId " +
                       "    WHERE S.genreId IS NOT NULL AND S.genreId != '' AND S.id REGEXP '^[0-9]+$' " +
                       "    AND S.movieId IS NOT NULL AND S.movieID != '' " +
                       ") " +
                       "SELECT C.id, C.name " +
                       "FROM cleaned AS C " +
                       "LEFT JOIN genres AS G ON G.id = C.genreId " +
                       "LEFT JOIN movies AS M ON M.id = M.movieId " +
                       "WHERE G.id IS NOT NULL " +
                       "WHERE M.id IS NOT NULL";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    protected void reportErrors() throws SQLException {

    }
}
