package ewmservice.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "name")
@Builder
public class NewCategoryDto {
    @NotBlank
    private String name;
}
