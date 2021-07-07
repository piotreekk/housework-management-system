package pl.strumnik.hms.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.strumnik.hms.repository.UserAccountRepository;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        return userAccountRepository.findByLoginWithRoles(login)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException("UserAccount with login [" + login + "] was not found in the database"));
    }
}
