package pl.strumnik.hms.domain;

import lombok.Getter;
import pl.strumnik.hms.exception.ErrorCode;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Housework extends BaseEntity {

    private String name;
    private String description;
    private LocalDate scheduledAt;
    private LocalDateTime assignedAt;
    private LocalDateTime finishedAt;
    @Enumerated(EnumType.STRING)
    private HouseworkStatus status;
    private String executorComment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "house_id", updatable = false)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserAccount assignedUser;

    @OneToMany(mappedBy = "housework", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HouseworkRating> ratings = new HashSet<>();

    @Deprecated
    protected Housework() {
    }

    private Housework(String name, String description, LocalDate scheduledAt, House house, UserAccount assignedUser) {
        ExceptionUtils.notBlank(name, "SCIN_PS_20210706212208");
        ExceptionUtils.notBlank(description, "SCIN_PS_20210706212224");
        ExceptionUtils.notNull(scheduledAt, "SCIN_PS_20210706212236");
        ExceptionUtils.notNull(house, "SCIN_PS_20210706212243");
        ExceptionUtils.notNull(assignedUser, "SCIN_PS_20210706212250");

        this.name = name;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.assignedAt = null;
        this.finishedAt = null;
        this.status = HouseworkStatus.TO_DO;
        this.executorComment = null;
        this.house = house;
        this.house.addHousework(this, PrivateInvocationToken.INSTANCE);
        this.assignedUser = assignedUser;
    }

    public static Housework create(String name, String description, LocalDate scheduledAt,
                                   House house, UserAccount userResponsible) {
        return new Housework(name, description, scheduledAt, house, userResponsible);
    }

    public void update(String name, String description, LocalDate scheduledAt) {
        ExceptionUtils.notBlank(name, "SCIN_PS_20210707224854");
        ExceptionUtils.notBlank(description, "SCIN_PS_20210707224902");
        ExceptionUtils.notNull(scheduledAt, "SCIN_PS_20210707224913");

        if (this.status != HouseworkStatus.TO_DO)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210705223208", HouseworkErrorCode.HOUSEWORK_NOT_TO_DO);

        this.name = name;
        this.description = description;
        this.scheduledAt = scheduledAt;
    }

    public void assignHousework(UserAccount user) {
        ExceptionUtils.notNull(user, "SCIN_PS_20210628205730");
        if (this.assignedUser != null)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210628210219", HouseworkErrorCode.HOUSEWORK_ALREADY_ASSIGNED);
        if (this.status != HouseworkStatus.TO_DO)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210704221436", HouseworkErrorCode.HOUSEWORK_NOT_TO_DO);

        this.assignedUser = user;
        this.assignedUser.addHousework(this, PrivateInvocationToken.INSTANCE);
        this.status = HouseworkStatus.IN_PROGRESS;
    }

    public void resignFromHousework(UserAccount user) {
        ExceptionUtils.notNull(user, "SCIN_PS_20210704221744");
        if (this.assignedUser == null)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210704221746", HouseworkErrorCode.HOUSEWORK_NOT_ASSIGNED);
        if (!this.assignedUser.equals(user))
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210706211810", HouseworkErrorCode.HOUSEWORK_ASSIGNED_TO_ANOTHER_USER);
        if (this.status != HouseworkStatus.IN_PROGRESS)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210704221748", HouseworkErrorCode.HOUSEWORK_NOT_TO_DO);

        this.assignedUser.removeHousework(this, PrivateInvocationToken.INSTANCE);
        this.assignedUser = null;
        this.status = HouseworkStatus.TO_DO;
    }

    public void finishHousework(UserAccount user, String comment) {
        if (this.assignedUser == null)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210628211157", HouseworkErrorCode.HOUSEWORK_NOT_ASSIGNED);
        if (!this.assignedUser.equals(user))
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210628211305", HouseworkErrorCode.HOUSEWORK_ASSIGNED_TO_ANOTHER_USER);
        if (this.status != HouseworkStatus.IN_PROGRESS)
            ExceptionUtils.throwAppBusinessException("SCIN_PS_20210628211853", HouseworkErrorCode.HOUSEWORK_NOT_IN_PROGRESS);

        this.status = HouseworkStatus.FINISHED;
        this.executorComment = comment;
        this.finishedAt = LocalDateTime.now();
    }

    void addRating(HouseworkRating houseworkRating, HouseworkRating.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628205312");
        if (this.ratings.contains(houseworkRating))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628205324");

        this.ratings.add(houseworkRating);
    }

    public static enum HouseworkErrorCode implements ErrorCode {
        HOUSEWORK_NOT_FOUND("Nie znaleziono zadania."),
        HOUSEWORK_ALREADY_ASSIGNED("Zadanie jest już przypisane do użytkownika"),
        HOUSEWORK_NOT_ASSIGNED("Zadanie nie jest przypisane do żadnego użytkownika"),
        HOUSEWORK_ASSIGNED_TO_ANOTHER_USER("Zadanie jest przypisane do innego użytkownika"),
        HOUSEWORK_NOT_TO_DO("Zadanie nie ma statusu 'Do wykonania'"),
        HOUSEWORK_NOT_IN_PROGRESS("Zadanie nie ma statusu 'W takcie'");

        private String message;

        HouseworkErrorCode(String message) {
            this.message = message;
        }

        @Override
        public String provideMessage() {
            return message;
        }
    }

    static final class PrivateInvocationToken {
        private static PrivateInvocationToken INSTANCE = new PrivateInvocationToken();

        private PrivateInvocationToken() {
        }
    }
}
