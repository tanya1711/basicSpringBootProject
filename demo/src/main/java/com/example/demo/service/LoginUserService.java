package com.example.demo.service;

import com.example.demo.model.Form;
import com.example.demo.rep.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Form user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();
        }


    }

}
