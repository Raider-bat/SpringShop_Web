package kz.gexa.spring.shop.service;

import kz.gexa.spring.shop.entity.user.Role;
import kz.gexa.spring.shop.entity.user.User;
import kz.gexa.spring.shop.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserService  implements UserDetailsService {

    final
    UserRepo userRepo;
    final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void registrationUser(User user){
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
    }

    public void changePassword(User user, User userExist, String newPassword){
        user.setId(userExist.getId());
        user.setActive(true);
        user.setRoles(userExist.getRoles());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        log.info("USER:" + user.toString() + " ОБНОВЛЁН");
    }

    public boolean isAdmin(String username){
        boolean isAdmin = false;

        if (username != null){
            User user = userRepo.findByUsername(username);
            isAdmin = user.getRoles().stream().anyMatch(role -> role == Role.ADMIN);
        }
        return isAdmin;
    }
}