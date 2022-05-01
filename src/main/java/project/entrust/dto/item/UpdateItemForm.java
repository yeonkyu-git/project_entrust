package project.entrust.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemForm {

    private Long memberId;

    @NotBlank
    private Long itemId;

    @NotBlank
    private String itemName;

    @NotBlank
    private String description;
}
