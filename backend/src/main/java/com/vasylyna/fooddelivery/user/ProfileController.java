package com.vasylyna.fooddelivery.user;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/profile") public class ProfileController{
 private final AppUserRepository users;public ProfileController(AppUserRepository users){this.users=users;}
 @GetMapping public Profile get(Authentication a){return profile(find(a.getName()));}
 @PutMapping public Profile update(Authentication a,@Valid @RequestBody UpdateProfile r){AppUser u=find(a.getName());u.updateProfile(r.fullName().trim());return profile(users.save(u));}
 private AppUser find(String email){return users.findByEmailIgnoreCase(email).orElseThrow();}private Profile profile(AppUser u){return new Profile(u.getId(),u.getFullName(),u.getEmail(),u.getRole().name());}
 public record Profile(Long id,String fullName,String email,String role){}public record UpdateProfile(@NotBlank @Size(max=120) String fullName){}
}
