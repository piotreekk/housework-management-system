package pl.strumnik.hms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.strumnik.hms.domain.RoleName;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserAccountId();
        }
        return null;
    }

    public static Set<RoleName> getLoggedUserRolesNames() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getAuthorities().stream()
                    .map(RoleName::fromGrantedAuthority)
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    public static boolean hasLoggedUserRole(RoleName roleName) {
        Set<RoleName> loggedUserRolesNames = getLoggedUserRolesNames();
        return loggedUserRolesNames.contains(roleName);
    }
}
