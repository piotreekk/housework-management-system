package pl.strumnik.hms.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.strumnik.hms.domain.Role;
import pl.strumnik.hms.domain.RoleName;
import pl.strumnik.hms.domain.UserAccount;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private Long userAccountId;
    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(UserAccount userAccount) {
        List<GrantedAuthority> roles = userAccount.getRoles().stream()
                .map(Role::getName)
                .map(RoleName::toGrantedAuthority)
                .collect(Collectors.toList());

        return new UserPrincipal(
                userAccount.getId(),
                userAccount.getLogin(),
                userAccount.getPassword(),
                userAccount.isEnabled(),
                roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
