package com.example.ms_usuarios.Service;

import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Repository.UserRepository;
import com.example.ms_usuarios.Repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private RolRepository rolRepository;
    private UserService service;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        rolRepository = mock(RolRepository.class);
        service = new UserService(userRepository, rolRepository);
    }

    // ======================
    // Crear usuario OK
    // ======================
    @Test
    void testCrearUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Xiomara");
        u.setApellido("Romero");
        u.setEmail("xio@test.cl");
        u.setPassword("123456");

        when(userRepository.existsByEmail("xio@test.cl")).thenReturn(false);
        when(userRepository.save(Mockito.any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario result = service.create(u);

        assertNotNull(result);
        assertEquals("Xiomara", result.getNombre());
        assertTrue(BCrypt.checkpw("123456", result.getPassword()));
    }

    // ======================
    // Email duplicado
    // ======================
    @Test
    void testCrearUsuarioEmailDuplicado() {

        Usuario u = new Usuario();
        u.setNombre("Laura");
        u.setApellido("Soto");
        u.setEmail("laura@test.cl");
        u.setPassword("123456");

        when(userRepository.existsByEmail("laura@test.cl")).thenReturn(true);

        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> service.create(u));

        assertEquals("El correo ya está registrado", ex.getMessage());
    }

    // ======================
    // Login OK
    // ======================
    @Test
    void testLoginExitoso() {
        Usuario u = new Usuario();
        u.setEmail("admin@test.cl");
        u.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));

        when(userRepository.findByEmail("admin@test.cl"))
                .thenReturn(Optional.of(u));

        Usuario result = service.login("admin@test.cl", "123456");

        assertNotNull(result);
    }

    // ======================
    // Password incorrecta
    // ======================
    @Test
    void testLoginPasswordIncorrecta() {
        Usuario u = new Usuario();
        u.setEmail("admin@test.cl");
        u.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));

        when(userRepository.findByEmail("admin@test.cl"))
                .thenReturn(Optional.of(u));

        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> service.login("admin@test.cl", "000000"));

        assertEquals("Contraseña incorrecta", ex.getMessage());
    }
}
