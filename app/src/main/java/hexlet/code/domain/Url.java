package hexlet.code.domain;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
public final class Url {

    private long id;
    private String name;
    private Timestamp createdAt;

    public Url(String urlName) {
        this.name = urlName;
    }

    public Instant getCreatedAt() {
        return this.createdAt.toInstant();
    }
}
