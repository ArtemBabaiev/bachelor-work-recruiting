package edu.chnu.recruiting.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with name " + username + "doesnt exists"));
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), 
				user.getPassword(),
				user.isEnabled(), 
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				getAuthorities(user.getRoles()));
	}

	private Set<GrantedAuthority> getAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toSet());
	}
}
