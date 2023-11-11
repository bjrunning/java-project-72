package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    @Test
    void testInit() {
        assertThat(true).isEqualTo(true);
    }

    private static Javalin app;
    private static MockWebServer mockServer;
    private static String mockUrl;

    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        mockUrl = "http://localhost:" + port;

        mockServer = new MockWebServer();
        Path path  = Paths.get("src/test/resources/sample.html").toAbsolutePath().normalize();

        MockResponse mockedResponse = new MockResponse()
                .setBody(readString(path));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        mockServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    void testIndex() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/").code()).isEqualTo(200);
        });
    }

    @Test
    void testIndex1() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");

            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + mockUrl;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            Url actualUrl = UrlsRepository.findByName(mockUrl).orElse(null);
            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(mockUrl);
        });
    }

    @Test
    public void testUrlPage() throws Exception {
        var url = new Url(mockUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCheckUrl() throws Exception {
        String url = mockServer.url("/").toString().replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            var actualUrl = UrlsRepository.findByName(url).orElse(null);

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(url);

            client.post("/urls/" + actualUrl.getId() + "/checks");

            var response = client.get("/urls/" + actualUrl.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(url);

            var actualCheckUrl = UrlChecksRepository
                    .findLatestChecks().get(actualUrl.getId());

            assertThat(actualCheckUrl).isNotNull();
            assertThat(actualCheckUrl.getStatusCode()).isEqualTo(200);
            assertThat(actualCheckUrl.getTitle()).isEqualTo("Sample title");
            assertThat(actualCheckUrl.getH1()).isEqualTo("Sample header");
            assertThat(actualCheckUrl.getDescription()).contains("description");
        });
    }
}
