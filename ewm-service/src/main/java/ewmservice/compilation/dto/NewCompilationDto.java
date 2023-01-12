package ewmservice.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "title")
@Builder
public class NewCompilationDto {
    private Long id;
    private Boolean pinned;
    @NotBlank
    private String title;
    private Set<Long> events;
}
