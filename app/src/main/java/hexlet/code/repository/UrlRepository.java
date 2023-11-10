package hexlet.code.repository;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import hexlet.code.BaseRepository;
import hexlet.code.domain.Url;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls(name, created_at) VALUES(?,?);";
        var conn = BaseRepository.getDataSource().getConnection();
        var dateTime = new Timestamp(System.currentTimeMillis());
        try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, dateTime);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(1L);
                url.setCreatedAt(dateTime);
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Url find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
            return null;
        }
    }

    public static Url findByName(String urlName) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";

        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var url = new Url(urlName);
                var createdAt = resultSet.getTimestamp("created_at");

                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
            return null;
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";

        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var createdAt = resultSet.getTimestamp("created_at");
                var name = resultSet.getString("name");
                var url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }
}
