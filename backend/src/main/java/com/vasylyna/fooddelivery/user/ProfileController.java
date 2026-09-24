package com.vasylyna.fooddelivery.user;

import com.vasylyna.fooddelivery.user.dto.ProfileResponse;
import com.vasylyna.fooddelivery.user.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ProfileResponse get(Authentication authentication) {
        return service.getProfile(authentication.getName());
    }

    @PutMapping
    public ProfileResponse update(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return service.updateProfile(authentication.getName(), request.fullName());
    }
}
