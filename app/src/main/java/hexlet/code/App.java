package hexlet.code;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.AppUtils.addRoutes;
import static hexlet.code.AppUtils.getPort;
import static hexlet.code.AppUtils.getDatabaseUrl;
import static hexlet.code.AppUtils.isProduction;
import static hexlet.code.AppUtils.getTemplateEngine;

public class App {
    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        System.setProperty("h2.traceLevel", "TRACE_LEVEL_SYSTEM_OUT=3");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResource("schema.sql");
        var file = new File(url.getFile());
        var sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.setDataSource(dataSource);

        Javalin app = Javalin.create(config -> {
            if (!isProduction()) {
                config.plugins.enableDevLogging();
            }
            JavalinThymeleaf.init(getTemplateEngine());
        });

        addRoutes(app);
        app.before(ctx -> ctx.attribute("ctx", ctx));
        return app;
    }
}
