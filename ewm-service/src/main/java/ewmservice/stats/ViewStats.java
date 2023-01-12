package ewmservice.stats;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
