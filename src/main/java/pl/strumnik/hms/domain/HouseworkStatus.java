package pl.strumnik.hms.domain;

import pl.strumnik.hms.exception.ExceptionUtils;

import java.util.Map;

public enum HouseworkStatus {
    TO_DO,
    IN_PROGRESS,
    FINISHED;

    private static Map<HouseworkStatus, String> houseworkStatusToDisplayName = Map.of(
            TO_DO, "Do wykonania",
            IN_PROGRESS, "W trakcie",
            FINISHED, "Wykonane"
    );

    public String getDisplayName() {
        if (!houseworkStatusToDisplayName.containsKey(this))
            ExceptionUtils.throwAppIllegalArgumentException("SCIN_PS_20210703153530");

        return houseworkStatusToDisplayName.get(this);
    }
}
