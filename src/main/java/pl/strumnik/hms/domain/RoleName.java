package pl.strumnik.hms.domain;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;

public enum RoleName {
    USER,
    ADMIN;

    private static Map<RoleName, GrantedAuthority> roleNameToGrantedAuthority = Map.of(
            ADMIN, new SimpleGrantedAuthority(ADMIN.name()),
            USER, new SimpleGrantedAuthority(USER.name())
    );

    private static Map<GrantedAuthority, RoleName> grantedAuthorityRoleName =
            MapUtils.invertMap(roleNameToGrantedAuthority);

    public static RoleName fromGrantedAuthority(GrantedAuthority grantedAuthority) {
        return grantedAuthorityRoleName.get(grantedAuthority);
    }

    public GrantedAuthority toGrantedAuthority() {
        return roleNameToGrantedAuthority.get(this);
    }
}
