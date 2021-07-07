package pl.strumnik.hms.domain;

import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String uuid = UUID.randomUUID().toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        final Class thisClass = Hibernate.getClass(this);
        final Class otherClass = Hibernate.getClass(o);

        if (o instanceof BaseEntity) {
            final BaseEntity other = (BaseEntity) o;
            return thisClass.equals(otherClass)
                    && (Objects.equals(getId(), other.getId()) || Objects.equals(getUuid(), other.getUuid()));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
