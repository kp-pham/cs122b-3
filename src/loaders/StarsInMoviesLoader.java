package loaders;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StarsInMoviesLoader extends DataLoader {
    public StarsInMoviesLoader(Connection conn) {
        super(conn);
    }

    @Override
    protected void createStagingTable() throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS stars_in_movies_staging";
        String createQuery = "CREATE TABLE stars_in_movies_staging( " +
                             "    starId TEXT, " +
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
                       "INTO TABLE stars_in_movies_staging " +
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
        String query = "INSERT INTO stars_in_movies (starId, movieId) " +
                       "WITH deduped AS ( " +
                       "    SELECT id " +
                       "    FROM stars_in_movies_staging " +
                       "    GROUP BY id " +
                       "    HAVING COUNT(*) = 1 " +
                       "), " +
                       "cleaned AS ( " +
                       "    SELECT S.starId, S.movieId " +
                       "    FROM stars_in_movies_staging AS S " +
                       "    INNER JOIN deduped AS D ON D.id = S.id " +
                       "    WHERE S.starId IS NOT NULL AND S.starId != '' " +
                       "    AND S.movieId IS NOT NULL AND S.movieId != '' " +
                       ") " +
                       "SELECT C.starId, C.movieId " +
                       "FROM cleaned AS C " +
                       "LEFT JOIN stars AS S ON S.id = C.starId " +
                       "LEFT JOIN movies AS M ON M.id = C.movieId " +
                       "WHERE S.id IS NULL " +
                       "WHERE M.id IS NULL";

        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    protected void reportErrors() throws SQLException {

    }
}
