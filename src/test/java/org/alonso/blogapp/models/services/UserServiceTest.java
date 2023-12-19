package org.alonso.blogapp.models.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.dto.enums.ERole;
import org.alonso.blogapp.models.entities.Region;
import org.alonso.blogapp.models.entities.Role;
import org.alonso.blogapp.models.entities.UserEntity;
import org.alonso.blogapp.models.repositories.RegionRepository;
import org.alonso.blogapp.models.repositories.RoleRepository;
import org.alonso.blogapp.models.repositories.UserRepository;
import org.alonso.blogapp.models.services.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private UserServiceImpl userService;

    CreateUserDTO createUserDTO;
    UpdateUserDTO updateUserDTO;
    UserEntity user;
    UserEntity user2;
    UserEntity user3;

    @BeforeEach
    void beforeAll() {
        createUserDTO = new CreateUserDTO("Test", "Test", "testing123", "test@test.com", "ASdf123$",
                "ASdf123$", "32354354", LocalDate.of(1992, 9, 21), 1L, null);

        updateUserDTO = new UpdateUserDTO("Testing2", "Testing2", "testing456", "testing-17348398989", "4353463",
                LocalDate.of(1992, 9, 21), 2L);

        user = new UserEntity("550e8400-e29b-41d4-a716-446655440000", "Test", "Test", "testing123",
                "testing-17348398989", "test@test.com", "$2a$12$BX.mva.bha8VEZxOgX06TOmnqj230VsYw60jR125z4djBK8sdBjQ2",
                "32354354", LocalDate.of(1992, 9, 21),
                new Region(1L, "Norteamerica"), null, true, false, true, LocalDateTime.now(), LocalDateTime.now(),
                Arrays.asList(new Role(1L, ERole.valueOf("ROLE_USER"))));

        user2 = new UserEntity("650e8400-e29b-41d4-a716-446655448888", "Testing2", "Testing2", "testing456",
                "testing-17348398989", "test2@test.com", "1234", "4353463", LocalDate.of(1992, 9, 21),
                new Region(2L, "Sudamerica"), null, true, false, true, LocalDateTime.now(), LocalDateTime.now(),
                Arrays.asList(new Role(1L, ERole.valueOf("ROLE_USER"))));

        user3 = new UserEntity("550e8400-e29b-41d4-a716-446655440000", "Test", "Test", "testing123",
                "testing-17348398989", "test@test.com", "$2a$12$BX.mva.bha8VEZxOgX06TOmnqj230VsYw60jR125z4djBK8sdBjQ2",
                "32354354", LocalDate.of(1992, 9, 21),
                new Region(1L, "Norteamerica"), null, false, false, true, LocalDateTime.now(), LocalDateTime.now(),
                Arrays.asList(new Role(1L, ERole.valueOf("ROLE_USER"))));
    }

    @Test
    void testFindAll() {
        when(userRepository.getAll()).thenReturn(Arrays.asList(user, user2));
        List<UserDTO> users = userService.findAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).getAll();
    }

    @Test
    void testFindOne() {
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(Optional.of(user));

        UserDTO user = userService.findOne("550e8400-e29b-41d4-a716-446655440000");
        assertNotNull(user);
        assertTrue(user.getActive());

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655440000");
    }

    @Test
    void testFindOneSlug() {
        when(userRepository.getBySlug("testing-17348398989")).thenReturn(Optional.of(user));

        UserDTO user = userService.findOne("testing-17348398989");
        assertNotNull(user);
        assertTrue(user.getActive());

        verify(userRepository, times(1)).getById("testing-17348398989");
    }

    @Test
    void testFindOneNull() {
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655441111")).thenReturn(Optional.empty());
        when(userRepository.getBySlug("550e8400-e29b-41d4-a716-446655441111")).thenReturn(Optional.empty());

        UserDTO user = userService.findOne("550e8400-e29b-41d4-a716-446655441111");
        assertNull(user);

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655441111");
    }

    @Test
    void testUpdate() {
        when(userRepository.save(user)).thenReturn(user2);
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(Optional.of(user));
        when(regionRepository.findById(2L)).thenReturn(Optional.of(new Region(2L, "Sudamerica")));

        UserDTO userDTO = userService.update(updateUserDTO, "550e8400-e29b-41d4-a716-446655440000");
        assertNotNull(userDTO);
        assertEquals(userDTO.getName(), user2.getFirstname() + " " + user2.getLastname());

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655440000");
        verify(userRepository, times(1)).save(user);
        verify(regionRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateNull() {
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655441111")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.update(updateUserDTO, "550e8400-e29b-41d4-a716-446655441111");
        });

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655441111");
    }

    @Test
    void testDelete() {
        when(userRepository.save(user)).thenReturn(user3);
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.delete("550e8400-e29b-41d4-a716-446655440000");
        assertNotNull(userDTO);
        assertFalse(userDTO.getActive());

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655440000");
    }

    @Test
    void testDeleteNull() {
        when(userRepository.getById("550e8400-e29b-41d4-a716-446655441111")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.delete("550e8400-e29b-41d4-a716-446655441111");
        });

        verify(userRepository, times(1)).getById("550e8400-e29b-41d4-a716-446655441111");
    }
}
