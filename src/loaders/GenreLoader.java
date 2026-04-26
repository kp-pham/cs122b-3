package loaders;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenreLoader extends DataLoader {
    public GenreLoader(Connection conn) { super(conn); }

    @Override
    protected void createStagingTable() throws SQLException {

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
