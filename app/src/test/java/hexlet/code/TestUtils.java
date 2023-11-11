package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    static Long findIdByUlrName(HikariDataSource dataSource, String urlName) {
        var sql = "SELECT id FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Url findUrlById(HikariDataSource dataSource, Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name);
                url.setCreatedAt(createdAt);
                url.setId(id);
                return url;
            }
            return null;
        }
    }

    static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    public static Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        var conn = BaseRepository.getDataSource().getConnection();
        try (var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new HashMap<Long, UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at");
                var check = new UrlCheck(statusCode, title, h1, description, urlId);
                check.setId(id);
                check.setCreatedAt(createdAt);
                result.put(urlId, check);
            }
            return result;
        }
    }
}
