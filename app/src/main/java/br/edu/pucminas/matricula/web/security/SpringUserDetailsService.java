package br.edu.pucminas.matricula.web.security;

import br.edu.pucminas.matricula.infrastructure.persistence.UserEntity;
import br.edu.pucminas.matricula.infrastructure.persistence.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringUserDetailsService implements UserDetailsService {
  private final UserJpaRepository userJpaRepository;

  public SpringUserDetailsService(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userJpaRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new AppUserPrincipal(user.getId(), user.getUsername(), user.getPasswordHash(), user.getRole());
  }
}
