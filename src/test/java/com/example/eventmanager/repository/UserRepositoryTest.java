package com.example.eventmanager.repository;

import com.example.eventmanager.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByUsername() {
        User user = new User();
        user.setUsername("john123");
        user.setEmail("john@example.com");
        user.setPassword("secret");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(User.Role.USER);

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("john123");

        assertTrue(found.isPresent());
        assertEquals("john@example.com", found.get().getEmail());
    }

    @Test
    void testExistsByEmail() {
        User user = new User();
        user.setUsername("jane123");
        user.setEmail("jane@example.com");
        user.setPassword("secret");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(User.Role.USER);

        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("jane@example.com"));
        assertFalse(userRepository.existsByEmail("fake@example.com"));
    }
}
