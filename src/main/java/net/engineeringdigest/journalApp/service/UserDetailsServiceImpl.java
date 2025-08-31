package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntry user = usersRepository.findByPhoneNumber(username);
        if ( user != null){
            return User.builder()
                    .username(user.getPhoneNumber())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
       throw new UsernameNotFoundException("Username not found:" + username);
    }
}
