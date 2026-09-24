package com.vasylyna.fooddelivery.user.dto;

import com.vasylyna.fooddelivery.user.AppUser;

public record ProfileResponse(Long id, String fullName, String email, String role) {
    public static ProfileResponse from(AppUser user) {
        return new ProfileResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
