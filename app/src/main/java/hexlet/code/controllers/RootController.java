package hexlet.code.controllers;

import io.javalin.http.Context;
public class RootController {

    public static void welcome(Context ctx) {
        ctx.render("urls/index.html");
    }
}
