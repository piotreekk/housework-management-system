package pl.strumnik.hms.domain;

import lombok.Getter;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Entity
public class HouseworkRating extends BaseEntity {

    private Integer rate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Housework housework;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserAccount createdBy;

    @Deprecated
    protected HouseworkRating() {
    }

    private HouseworkRating(Integer rate, Housework housework, UserAccount createdBy) {
        ExceptionUtils.notNull(rate, "SCIN_PS_20210706212403");
        ExceptionUtils.notNull(housework, "SCIN_PS_20210706212411");
        ExceptionUtils.notNull(createdBy, "SCIN_PS_20210706212418");

        this.rate = rate;
        this.housework = housework;
        this.housework.addRating(this, PrivateInvocationToken.INSTANCE);
        this.createdBy = createdBy;
        this.createdBy.addPostedRating(this, PrivateInvocationToken.INSTANCE);
    }

    public static HouseworkRating create(Integer rate, Housework housework, UserAccount createdBy) {
        return new HouseworkRating(rate, housework, createdBy);
    }

    static final class PrivateInvocationToken {
        private static PrivateInvocationToken INSTANCE = new PrivateInvocationToken();

        private PrivateInvocationToken() {
        }
    }
}
