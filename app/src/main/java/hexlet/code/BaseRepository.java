package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    private static HikariDataSource dataSource;

    public static void setDataSource(HikariDataSource inputDataSource) {
        dataSource = inputDataSource;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
