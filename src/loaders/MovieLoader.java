package loaders;

import java.sql.Connection;
import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;

public class MovieLoader implements DataLoader {
    private final Connection conn;

    public MovieLoader(Connection conn) {
        this.conn = conn;
    }

    public Set<String> load(String file) {

    }
}
