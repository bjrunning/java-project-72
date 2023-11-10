package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static void addRoutes(Javalin app) {
        app.get("/", ctx -> {
            ctx.result("Hello World");
        });
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.plugins.enableDevLogging();
        });

        addRoutes(app);

        app.before(ctx -> {
            ctx.attribute("ctx", ctx);
        });

        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }
}