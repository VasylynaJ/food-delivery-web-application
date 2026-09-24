package com.vasylyna.fooddelivery.user;

import com.vasylyna.fooddelivery.user.dto.ProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final AppUserRepository users;

    public ProfileService(AppUserRepository users) {
        this.users = users;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        return ProfileResponse.from(users.findByEmailIgnoreCase(email).orElseThrow());
    }

    @Transactional
    public ProfileResponse updateProfile(String email, String fullName) {
        AppUser user = users.findByEmailIgnoreCase(email).orElseThrow();
        user.updateProfile(fullName.trim());
        return ProfileResponse.from(users.save(user));
    }
}
