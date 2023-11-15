package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

public class BaseRepository {
    @Getter
    private static HikariDataSource dataSource;

    public static void setDataSource(HikariDataSource inputDataSource) {
        dataSource = inputDataSource;
    }

}
