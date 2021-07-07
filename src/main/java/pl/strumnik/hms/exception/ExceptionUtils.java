package pl.strumnik.hms.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public final class ExceptionUtils {

    private ExceptionUtils() {
        throw new OperationNotSupportedException("SCIN_PS_20210627221053");
    }

    public static void notNull(Object object, String scin) {
        if (object == null)
            throwAppIllegalArgumentException(scin);
    }

    public static void notNullNotEmptyAndWithoutNulls(Collection<?> collection, String scinIfNull,
                                                      String scinIfIsEmpty, String scinIfHasNulls) {
        if (collection == null)
            throwAppIllegalArgumentException(scinIfNull);
        if (collection.isEmpty())
            throwAppIllegalArgumentException(scinIfIsEmpty);
        if (collection.stream().anyMatch(Objects::isNull))
            throwAppIllegalArgumentException(scinIfHasNulls);
    }

    public static void notBlank(CharSequence charSequence, String scin) {
        if (StringUtils.isBlank(charSequence))
            throwAppIllegalArgumentException(scin);
    }

    public static void throwAppIllegalArgumentException(String scin) {
        throw new AppIllegalArgumentException(scin);
    }

    public static void throwAppIllegalStateException(String scin) {
        throw new AppIllegalStateException(scin);
    }

    public static void throwAppBusinessException(String scin, ErrorCode errorCode) {
        throw new AppBusinessException(scin, errorCode);
    }

}
