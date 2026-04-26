package loaders;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StarLoader implements DataLoader {
    private final Connection conn;

    public StarLoader(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void load(String file) throws Exception {

    }
}
