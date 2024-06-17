package com.electronic.store.ElectronicStore.dtos;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto
{
    private String categoryId;

    @NotBlank
    @Size(min=4,message = "Give title length more than 3 characters")
    private String title;

    @NotBlank(message = "description required!!!")
    private String description;

    @NotBlank(message = "description required")
    private String coverImage;
}
