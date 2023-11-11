package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public final class Url {

    private long id;

    private String name;

    private Instant createdAt;

    public Url(String name) {
        this.name = name;
    }

}
