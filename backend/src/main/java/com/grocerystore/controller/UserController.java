package com.grocerystore.controller;
import com.grocerystore.dto.ApiResponse;
import com.grocerystore.entity.User;
import com.grocerystore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMe() {
        return ResponseEntity.ok(ApiResponse.ok("Current user", userService.getCurrentUser()));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<User>> updateMe(@RequestBody User updated) {
        User current = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", userService.updateProfile(current.getId(), updated)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("All users", userService.getAllUsers()));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("User status toggled", null));
    }
}
