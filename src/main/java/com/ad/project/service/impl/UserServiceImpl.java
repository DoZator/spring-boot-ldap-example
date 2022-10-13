package com.ad.project.service.impl;

import com.ad.project.domain.User;
import com.ad.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public User getMe() {
        var userData = getCurrentAuthUser();
        if (userData.isEmpty())
            return User.unauthorizedUser();

        return userData.get();
    }

    private Optional<User> getCurrentAuthUser() {
        var userPrincipal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (userPrincipal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return Optional.of(User.of(userDetails.getUsername()));
        } else {
            return Optional.empty();
        }
    }
}
