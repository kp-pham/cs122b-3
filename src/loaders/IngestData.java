package loaders;

import java.sql.Connection;
import java.sql.DriverManager;

public class IngestData {
    public static void main(String[] args) throws Exception {
        String username = System.getenv("USER");
        String password = System.getenv("PASSWORD");
        String url = System.getenv("URL") + "?allowLoadLocalInfile=true";

        Class.forName(System.getenv("JDBC_DRIVER"));
        Connection conn = DriverManager.getConnection(url, username, password);

        System.out.println("Loading movies...");

        MovieLoader movieLoader = new MovieLoader(conn);
        movieLoader.load("data/movies.csv");

        System.out.println("\nLoading movies completed");

        conn.close();
    }
}
