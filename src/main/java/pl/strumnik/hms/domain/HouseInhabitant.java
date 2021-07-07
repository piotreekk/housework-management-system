package pl.strumnik.hms.domain;

import lombok.Getter;
import pl.strumnik.hms.exception.ErrorCode;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Entity
public class HouseInhabitant extends BaseEntity {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private House house;

    @Deprecated
    protected HouseInhabitant() {
    }

    private HouseInhabitant(LocalDate dateFrom, LocalDate dateTo, UserAccount user, House house) {
        ExceptionUtils.notNull(dateFrom, "SCIN_PS_20210704135427");
        ExceptionUtils.notNull(user, "SCIN_PS_20210704135435");
        ExceptionUtils.notNull(house, "SCIN_PS_20210704135445");

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.user = user;
        this.house = house;
        this.user.addHouse(this, PrivateInvocationToken.INSTANCE);
        this.house.addInhabitant(this, PrivateInvocationToken.INSTANCE);
    }

    public static HouseInhabitant create(LocalDate dateFrom, LocalDate dateTo, UserAccount user, House house) {
        return new HouseInhabitant(dateFrom, dateTo, user, house);
    }

    public void update(LocalDate dateFrom, LocalDate dateTo) {
        ExceptionUtils.notNull(dateFrom, "SCIN_PS_20210704142059");

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public static enum HouseInhabitantErrorCode implements ErrorCode {
        HOUSE_INHABITANT_NOT_FOUND("Nie znaleziono mieszkańca.");

        private String message;

        HouseInhabitantErrorCode(String message) {
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
