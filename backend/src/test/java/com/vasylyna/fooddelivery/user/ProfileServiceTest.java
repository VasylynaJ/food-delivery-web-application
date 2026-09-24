package com.vasylyna.fooddelivery.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.user.dto.ProfileResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProfileServiceTest {
    @Test
    void getLoadsUserByAuthenticatedEmailAndMapsTheProfile() {
        AppUserRepository users = mock(AppUserRepository.class);
        AppUser user = new AppUser("Test Person", "person@example.com", "hash", Role.CUSTOMER);
        when(users.findByEmailIgnoreCase("person@example.com")).thenReturn(Optional.of(user));

        ProfileResponse response = new ProfileService(users).getProfile("person@example.com");

        assertThat(response.fullName()).isEqualTo("Test Person");
        assertThat(response.email()).isEqualTo("person@example.com");
        assertThat(response.role()).isEqualTo("CUSTOMER");
        verify(users).findByEmailIgnoreCase("person@example.com");
    }

    @Test
    void updateTrimsNameAndMapsTheAuthenticatedUsersProfile() {
        AppUserRepository users = mock(AppUserRepository.class);
        AppUser user = new AppUser("Old Name", "person@example.com", "hash", Role.CUSTOMER);
        when(users.findByEmailIgnoreCase("person@example.com")).thenReturn(Optional.of(user));
        when(users.save(user)).thenReturn(user);

        ProfileResponse response = new ProfileService(users)
                .updateProfile("person@example.com", "  New Name  ");

        assertThat(response.fullName()).isEqualTo("New Name");
        verify(users).save(user);
    }
}
