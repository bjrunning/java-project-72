package hexlet.code.domain;

import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UrlCheck {

    private long id;
    private long urlId;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private Timestamp createdAt;

    public UrlCheck(int urlStatusCode,
                    String urlTitle,
                    String urlH1,
                    String urlDescription,
                    long urlIdN
    ) {
        this.statusCode = urlStatusCode;
        this.title = urlTitle;
        this.h1 = urlH1;
        this.description = urlDescription;
        this.urlId = urlIdN;
    }

    public Instant getCreatedAt() {
        return this.createdAt.toInstant();
    }
}
