package loaders;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenreLoader extends DataLoader {
    public GenreLoader(Connection conn) { super(conn); }

    @Override
    protected void createStagingTable() throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS genres_staging";
        String createQuery = "CREATE TABLE stars_staging( " +
                             "    id TEXT, " +
                             "    name TEXT " +
                             ")";

        PreparedStatement statement = conn.prepareStatement(dropQuery);
        statement.executeUpdate();

        statement = conn.prepareStatement(createQuery);
        statement.executeUpdate();

        statement.close();
    }

    @Override
    protected void loadToStaging(String file) throws SQLException {

    }

    @Override
    protected void validateAndTransform() throws SQLException {

    }

    @Override
    protected void reportErrors() throws SQLException {

    }
}
