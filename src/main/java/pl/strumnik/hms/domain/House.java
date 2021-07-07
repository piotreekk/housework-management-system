package pl.strumnik.hms.domain;

import lombok.Getter;
import pl.strumnik.hms.exception.ErrorCode;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class House extends BaseEntity {

    private String name;
    private String description;
    private String addressLine1;
    private String addressLine2;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserAccount owner;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HouseInhabitant> inhabitants = new HashSet<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Housework> houseworks = new HashSet<>();

    @Deprecated
    protected House() {
    }

    private House(String name, String description, String addressLine1, String addressLine2, UserAccount owner) {
        ExceptionUtils.notBlank(name, "SCIN_PS_20210704135727");
        ExceptionUtils.notBlank(description, "SCIN_PS_20210704135758");
        ExceptionUtils.notBlank(addressLine1, "SCIN_PS_20210704135800");
        ExceptionUtils.notBlank(addressLine2, "SCIN_PS_20210704135802");
        ExceptionUtils.notNull(owner, "SCIN_PS_20210704135810");

        this.name = name;
        this.description = description;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.owner = owner;
        this.owner.addOwnedHouse(this, PrivateInvocationToken.INSTANCE);
    }

    public static House create(String name, String description, String addressLine1, String addressLine2, UserAccount owner) {
        return new House(name, description, addressLine1, addressLine2, owner);
    }

    public void update(String name, String description, String addressLine1, String addressLine2) {
        ExceptionUtils.notBlank(name, "SCIN_PS_20210704135831");
        ExceptionUtils.notBlank(description, "SCIN_PS_20210704135833");
        ExceptionUtils.notBlank(addressLine1, "SCIN_PS_20210704135835");
        ExceptionUtils.notBlank(addressLine2, "SCIN_PS_20210704135836");

        this.name = name;
        this.description = description;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }

    public long getInhabitantsCount() {
        return inhabitants.stream()
                .filter(houseInhabitant -> houseInhabitant.getDateTo() == null
                        || !houseInhabitant.getDateTo().isBefore(LocalDate.now()))
                .map(HouseInhabitant::getUser)
                .map(BaseEntity::getId)
                .distinct()
                .count();
    }

    void addInhabitant(HouseInhabitant inhabitant, HouseInhabitant.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628205144");
        if (this.inhabitants.contains(inhabitant))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628205201");

        this.inhabitants.add(inhabitant);
    }

    void addHousework(Housework housework, Housework.PrivateInvocationToken privateInvocationToken) {
        ExceptionUtils.notNull(privateInvocationToken, "SCIN_PS_20210628205527");
        if (this.houseworks.contains(housework))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210628205536");

        this.houseworks.add(housework);
    }

    private boolean hasInhabitant(final UserAccount user) {
        return inhabitants.stream()
                .map(HouseInhabitant::getUser)
                .anyMatch(u -> u.equals(user));
    }

    public static enum HouseErrorCode implements ErrorCode {
        HOUSE_NOT_FOUND("Nie znaleziono mieszkania.");

        private String message;

        HouseErrorCode(String message) {
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
