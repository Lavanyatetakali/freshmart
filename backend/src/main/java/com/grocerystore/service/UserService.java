package com.grocerystore.service;
import com.grocerystore.entity.User;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User updateProfile(Long id, User updated) {
        User user = getById(id);
        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setPhone(updated.getPhone());
        return userRepository.save(user);
    }

    public void toggleUserStatus(Long id) {
        User user = getById(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }
}
