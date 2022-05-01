package project.entrust.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemForm {

    private Long adminId;

    @NotBlank
    private Long ownerId;

    @NotBlank
    private String itemName;

    @NotBlank
    private String description;

    @NotBlank
    private Long categoryId;

}
