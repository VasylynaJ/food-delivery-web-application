package com.vasylyna.fooddelivery.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vasylyna.fooddelivery.common.ApiErrorResponseWriter;
import com.vasylyna.fooddelivery.order.AdminOrderController;
import com.vasylyna.fooddelivery.order.OrderController;
import com.vasylyna.fooddelivery.order.OrderService;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {OrderController.class, AdminOrderController.class})
@Import({SecurityConfiguration.class, JwtAuthenticationFilter.class, ApiErrorResponseWriter.class})
class SecurityAccessTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private AppUserRepository users;

    @MockBean
    private JwtService jwtService;

    @Test
    void unauthenticatedCustomerRouteReturnsTheApiJson401() throws Exception {
        mvc.perform(get("/api/orders"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Authentication required"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void customerCannotAccessAdminRoutesAndGetsTheApiJson403() throws Exception {
        mvc.perform(get("/api/admin/orders").with(user("customer@example.com").roles("CUSTOMER")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Access denied"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}