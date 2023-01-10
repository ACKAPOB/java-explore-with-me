package ru.practicum.stats.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ShortStatDto {
    @NotBlank
    @NotNull
    private String app;
    @NotBlank
    @NotNull
    private String uri;
    @NotBlank
    @NotNull
    private Long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortStatDto that = (ShortStatDto) o;
        return app.equals(that.app) && uri.equals(that.uri) && hits.equals(that.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}
