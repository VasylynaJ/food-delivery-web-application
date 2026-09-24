package com.vasylyna.fooddelivery.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.vasylyna.fooddelivery.auth.AuthController;
import com.vasylyna.fooddelivery.auth.dto.RegisterRequest;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import org.springframework.web.server.ResponseStatusException;

class ApiExceptionHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void notFoundUsesTheExistingNotFoundStatusAndErrorShape() {
        var response = handler.notFound(new ResourceNotFoundException("Menu item was not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("error", "Menu item was not found")
                .containsKeys("timestamp");
    }

    @Test
    void validationKeepsFieldErrorsAndDoesNotEchoRejectedValues() throws Exception {
        RegisterRequest request = new RegisterRequest("", "not-an-email", "password-that-must-not-be-returned");
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(request, "registerRequest");
        binding.addError(new FieldError("registerRequest", "email", "must be a well-formed email address"));
        Method method = AuthController.class.getMethod("register", RegisterRequest.class);
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(new MethodParameter(method, 0), binding);

        var response = handler.validation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Validation failed").containsKeys("timestamp", "fields");
        assertThat(((Map<?, ?>) response.getBody().get("fields")).get("email"))
                .isEqualTo("must be a well-formed email address");
        assertThat(response.getBody().toString()).doesNotContain("password-that-must-not-be-returned");
    }

    @Test
    void businessErrorsKeepTheirUsefulMessageAndBadRequestStatus() {
        var response = handler.businessRule(new BusinessRuleException("Your cart is empty"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Your cart is empty").containsKeys("timestamp");
    }

    @Test
    void unclassifiedIllegalArgumentsDoNotExposeInternalMessages() {
        var response = handler.invalidArgument(new IllegalArgumentException("database password: secret"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Invalid request");
        assertThat(response.getBody().toString()).doesNotContain("secret");
    }

    @Test
    void responseStatusErrorsPreserveStatusAndSafeReason() {
        var response = handler.responseStatus(
                new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("error", "Email is already registered");
    }

    @Test
    void unexpectedFailuresReturnOnlyAGenericMessage() {
        var response = handler.unexpected(new IllegalStateException("jdbc password=secret"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("error", "An unexpected error occurred");
        assertThat(response.getBody().toString()).doesNotContain("jdbc", "secret");
    }

    @Test
    void frameworkClientErrorsKeepTheirStatusAndReceiveASafeMessage() {
        var exception = new org.springframework.web.HttpRequestMethodNotSupportedException("TRACE");
        var response = handler.unexpected(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody()).containsEntry("error", "Method not allowed");
    }
}