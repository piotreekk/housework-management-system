package pl.strumnik.hms.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HouseInhabitantForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {Update.class}, message = "Nie wskazano mieszkańca do edycji.")
    private Long id;

    @NotNull(groups = {Save.class, Update.class}, message = "Nie wskazano mieszkania do powiązania.")
    private Long houseId;

    @NotNull(groups = {Save.class, Update.class}, message = "Nie wskazano użytkownika do powiązania.")
    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(groups = {Save.class, Update.class}, message = "Pole data od nie może być puste.")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    public interface Save { }

    public interface Update { }
}

