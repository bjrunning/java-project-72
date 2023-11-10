package hexlet.code;

import hexlet.code.controllers.RootController;
import hexlet.code.controllers.UrlsController;
import io.javalin.Javalin;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class AppUtils {

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "development");
    }

    static boolean isProduction() {
        return getMode().equals("production");
    }

    static int getPort() {
        return 7070;
    }

    static String getDatabaseUrl() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL",
                "jdbc:h2:mem:project");
    }

    static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine.addTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new Java8TimeDialect());

        return templateEngine;
    }

    static void addRoutes(Javalin app) {
        app.get("/", RootController::welcome);
        app.post("/urls", UrlsController::addUrl);
        app.get("/urls", UrlsController::listOfUrls);
        app.get("/urls/{id}", UrlsController::showUrl);
        app.post("/urls/{id}/checks", UrlsController::checks);
    }
}
