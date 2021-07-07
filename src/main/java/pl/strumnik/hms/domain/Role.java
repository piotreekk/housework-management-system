package pl.strumnik.hms.domain;

import lombok.Getter;
import pl.strumnik.hms.exception.ExceptionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Entity
public class Role extends BaseEntity {

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(length = 250)
    private String description;

    @Deprecated
    protected Role() {
    }

    private Role(RoleName name, String description) {
        ExceptionUtils.notNull(name, "SCIN_PS_20210706212453");
        ExceptionUtils.notBlank(description, "SCIN_PS_20210706212507");

        this.name = name;
        this.description = description;
    }

    public static Role create(RoleName name, String description) {
        return new Role(name, description);
    }
}
