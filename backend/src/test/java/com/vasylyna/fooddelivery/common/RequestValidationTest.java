package com.vasylyna.fooddelivery.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.vasylyna.fooddelivery.auth.dto.LoginRequest;
import com.vasylyna.fooddelivery.auth.dto.RegisterRequest;
import com.vasylyna.fooddelivery.cart.dto.AddCartItemRequest;
import com.vasylyna.fooddelivery.cart.dto.UpdateCartItemRequest;
import com.vasylyna.fooddelivery.menu.dto.MenuItemRequest;
import com.vasylyna.fooddelivery.order.dto.CheckoutRequest;
import com.vasylyna.fooddelivery.order.dto.UpdateOrderStatusRequest;
import com.vasylyna.fooddelivery.restaurant.dto.RestaurantRequest;
import com.vasylyna.fooddelivery.user.dto.UpdateProfileRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RequestValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void requestDtosRejectInvalidAuthProfileRestaurantMenuCartAndOrderFields() {
        assertViolations(new RegisterRequest("", "not-an-email", "short"), Set.of("fullName", "email", "password"));
        assertViolations(new LoginRequest("not-an-email", ""), Set.of("email", "password"));
        assertViolations(new UpdateProfileRequest(" "), Set.of("fullName"));
        assertViolations(new RestaurantRequest("", null, "", new BigDecimal("5.5"),
                new BigDecimal("-1.00"), "", null, null),
                Set.of("name", "cuisine", "rating", "deliveryFee", "openingHours"));
        assertViolations(new MenuItemRequest("", "", null, BigDecimal.ZERO, null, null),
                Set.of("category", "name", "price"));
        assertViolations(new AddCartItemRequest(null, 0), Set.of("menuItemId", "quantity"));
        assertViolations(new UpdateCartItemRequest(51), Set.of("quantity"));
        assertViolations(new CheckoutRequest("", "", "", null, null),
                Set.of("street", "city", "postalCode"));
        assertViolations(new UpdateOrderStatusRequest(null), Set.of("status"));
    }

    private void assertViolations(Object request, Set<String> expectedFields) {
        Set<String> fields = validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
        assertThat(fields).containsAll(expectedFields);
    }
}