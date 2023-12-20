package com.backend.expenseecho.security;


import com.backend.expenseecho.model.entities.UserInfo;
import com.backend.expenseecho.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = userRepository.findByEmail(username);
        if (userDetail.isPresent()) {
            return new UserInfoUserDetails(userDetail.get());
        } else {
            throw new UsernameNotFoundException("User account not found");
        }
    }
}
