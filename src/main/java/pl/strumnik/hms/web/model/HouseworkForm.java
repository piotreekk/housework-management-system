package pl.strumnik.hms.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HouseworkForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole nazwa nie może być puste")
    private String name;

    @NotBlank(groups = {Save.class, Update.class}, message = "Pole opis nie może być puste")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(groups = {Save.class, Update.class}, message = "Pole planowana data wykonania zadania nie może być puste")
    private LocalDate scheduledAt;

    @NotNull(groups = {Save.class, Update.class}, message = "Nie wskazano mieszkania, w którym będzie wykonywana praca")
    private Long houseId;

    private String status;

    private LocalDateTime finishedAt;

    private String executorComment;

    public interface Save {
    }

    public interface Update {
    }
}

