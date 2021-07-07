package pl.strumnik.hms.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HouseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole nazwa nie może być puste")
    private String name;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole opis nie może być puste")
    private String description;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole adres nie może być puste")
    private String addressLine1;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole adres cd. nie może być puste")
    private String addressLine2;

    public interface Save {}

    public interface Update {}
}

