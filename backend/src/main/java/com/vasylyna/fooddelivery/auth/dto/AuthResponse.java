package com.vasylyna.fooddelivery.auth.dto;

public record AuthResponse(String token, String tokenType, Long id, String fullName, String email, String role) {
}
