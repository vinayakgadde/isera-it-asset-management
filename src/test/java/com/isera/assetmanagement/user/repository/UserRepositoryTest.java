package com.isera.assetmanagement.user.repository;

import com.isera.assetmanagement.user.entity.User;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // =========================================================
    // TEST 1
    // Find user by username
    // =========================================================

    @Test
    void shouldFindUserByUsername() {

        Optional<User> result =
                userRepository.findByUsername("admin");

        assertTrue(result.isPresent());

        User user = result.get();

        assertEquals(
                "admin",
                user.getUsername()
        );

        assertTrue(
                user.getActive()
        );
    }

    // =========================================================
    // TEST 2
    // Find user by email
    // =========================================================

    @Test
    void shouldFindUserByEmail() {

        Optional<User> result =
                userRepository.findByEmail(
                        "employee1@isera.com"
                );

        assertTrue(result.isPresent());

        User user = result.get();

        assertEquals(
                "employee1@isera.com",
                user.getEmail()
        );

        assertEquals(
                "employee1",
                user.getUsername()
        );
    }

    // =========================================================
    // TEST 3
    // Find user by ID
    // =========================================================

    @Test
    void shouldFindUserById() {

        Optional<User> result =
                userRepository.findById(1L);

        assertTrue(result.isPresent());

        User user = result.get();

        assertEquals(
                1L,
                user.getId()
        );

        assertEquals(
                "admin",
                user.getUsername()
        );
    }

    // =========================================================
    // TEST 4
    // Find all users
    // =========================================================

    @Test
    void shouldFindAllUsers() {

        List<User> users =
                userRepository.findAll();

        assertNotNull(users);

        assertFalse(
                users.isEmpty()
        );
    }

    // =========================================================
    // TEST 5
    // Check username exists
    // =========================================================

    @Test
    void shouldReturnTrueWhenUsernameExists() {

        boolean exists =
                userRepository.existsByUsername(
                        "admin"
                );

        assertTrue(exists);
    }

    // =========================================================
    // TEST 6
    // Check username does not exist
    // =========================================================

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {

        boolean exists =
                userRepository.existsByUsername(
                        "user-does-not-exist-999"
                );

        assertFalse(exists);
    }

    // =========================================================
    // TEST 7
    // Check email exists
    // =========================================================

    @Test
    void shouldReturnTrueWhenEmailExists() {

        boolean exists =
                userRepository.existsByEmail(
                        "employee1@isera.com"
                );

        assertTrue(exists);
    }

    // =========================================================
    // TEST 8
    // Check email does not exist
    // =========================================================

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean exists =
                userRepository.existsByEmail(
                        "notfound999@isera.com"
                );

        assertFalse(exists);
    }

    // =========================================================
    // TEST 9
    // Verify roles are loaded with user
    // =========================================================

    @Test
    void shouldLoadUserRoles() {

        Optional<User> result =
                userRepository.findByUsername("admin");

        assertTrue(result.isPresent());

        User user = result.get();

        assertNotNull(
                user.getRoles()
        );

        assertFalse(
                user.getRoles().isEmpty()
        );

        assertTrue(
                user.getRoles()
                        .stream()
                        .anyMatch(
                                role ->
                                        "ADMIN".equals(
                                                role.getName()
                                        )
                        )
        );
    }

    // =========================================================
    // TEST 10
    // Unknown user should return empty
    // =========================================================

    @Test
    void shouldReturnEmptyWhenUserDoesNotExist() {

        Optional<User> result =
                userRepository.findByUsername(
                        "unknown-user-999"
                );

        assertTrue(
                result.isEmpty()
        );
    }
}