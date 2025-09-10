package ru.practicum.main.service.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.service.validator.SizeAfterTrim;

@Getter
@Setter
public class CategoryDto {
    @Min(1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @SizeAfterTrim(min = 1, max = 50)
    private String name;
}
