package customers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "customers.FullTextSearchServlet", urlPatterns = "/api/customers/full-text")
public class FullTextSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String title = request.getParameter("title");

        PrintWriter out = response.getWriter();

        if (title == null || title.trim().isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", "Please enter a search term.");
            out.write(jsonObject.toString());

            response.setStatus(400);
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT M.id, M.title, M.year, M.director, R.rating, " +
                           "CONCAT('[', GROUP_CONCAT(DISTINCT G.name ORDER BY G.name ASC SEPARATOR ', '), ']') AS genres, " +
                           "CONCAT('[', GROUP_CONCAT(DISTINCT JSON_OBJECT('id', S.id, 'name', S.name) ORDER BY S.movie_count DESC, S.name ASC), ']') AS stars " +
                           "FROM movies AS M " +
                           "LEFT JOIN genres_in_movies AS GIM ON M.id = GIM.movieId " +
                           "LEFT JOIN genres AS G ON GIM.genreId = G.id " +
                           "LEFT JOIN stars_in_movies AS SIM ON M.id = SIM.movieId " +
                           "LEFT JOIN (" +
                           "    SELECT S.id, S.name, COUNT(SIM.movieId) AS movie_count " +
                           "    FROM stars_in_movies AS SIM " +
                           "    LEFT JOIN stars AS S ON SIM.starId = S.id " +
                           "    GROUP BY S.id " +
                           ") AS S ON SIM.starId = S.id " +
                           "LEFT JOIN ratings AS R ON R.movieId = M.id " +
                           "WHERE MATCH (M.title) AGAINST (? IN BOOLEAN MODE) " +
                           "GROUP BY M.id, M.title, M.year, M.director, R.rating ";

            PreparedStatement statement = conn.prepareStatement(query);


        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", e.getMessage());
            out.write(jsonObject.toString());

            request.getServletContext().log("Error:", e);
            response.setStatus(500);

        } finally {
            out.close();
        }
    }
}
