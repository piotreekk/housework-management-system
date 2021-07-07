package pl.strumnik.hms.domain;

import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import pl.strumnik.hms.exception.ErrorCode;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class UserAccount extends BaseEntity {

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @NotNull
    @Size(min = 6, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean enabled = false;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<Role> roles;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HouseworkRating> postedRatings = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<House> ownedHouses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HouseInhabitant> houses = new HashSet<>();

    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL)
    private Set<Housework> houseworks = new HashSet<>();

    @Deprecated
    protected UserAccount() {
    }

    private UserAccount(String login, String password, String email, Set<Role> roles) {
        ExceptionUtils.notBlank(login, "SCIN_PS_20210706212533");
        ExceptionUtils.notBlank(password, "SCIN_PS_20210706212556");
        ExceptionUtils.notBlank(email, "SCIN_PS_20210706212559");
        ExceptionUtils.notNullNotEmptyAndWithoutNulls(roles, "SCIN_PS_20210627221803",
                "SCIN_PS_20210627221804", "SCIN_PS_20210627221811");

        this.login = login;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public static UserAccount create(String login, String password, String email, Set<Role> roles) {
        return new UserAccount(login, password, email, roles);
    }

    void addPostedRating(HouseworkRating houseworkRating,
                         HouseworkRating.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628204729");
        if (this.postedRatings.contains(houseworkRating))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628205023");

        this.postedRatings.add(houseworkRating);
    }

    void addOwnedHouse(House house, House.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628204808");
        if (this.ownedHouses.contains(house))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628205056");

        this.ownedHouses.add(house);
    }

    void addHouse(HouseInhabitant houseInhabitant, HouseInhabitant.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210703203441");
        if (this.houses.contains(houseInhabitant))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210703203444");

        this.houses.add(houseInhabitant);
    }

    void addHousework(Housework housework, Housework.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628210744");
        if (this.houseworks.contains(housework))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628210754");

        this.houseworks.add(housework);
    }

    void removeHousework(Housework housework, Housework.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210704221850");
        if (!this.houseworks.contains(housework))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210704221857");

        this.houseworks.remove(housework);
    }

    public static enum UserAccountErrorCode implements ErrorCode {
        USER_ACCOUNT_NOT_FOUND("Nie znaleziono użytkownika.");

        private String message;

        UserAccountErrorCode(String message) {
            this.message = message;
        }

        @Override
        public String provideMessage() {
            return message;
        }
    }

}
