package com.example.ms_usuarios.Controller;

import com.example.ms_usuarios.Model.Rol;
import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==========================================================
    // TEST LISTAR USUARIOS
    // ==========================================================
    @Test
    void listarUsuarios_devuelveListaUsuarios() throws Exception {

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");

        Usuario usuario = new Usuario(
                1L, "Juan", "Pérez", "juan@test.com",
                "123456", "99999999", "Santiago", rol
        );

        when(userService.getAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@test.com"));
    }


    // ==========================================================
    // TEST BUSCAR POR ID
    // ==========================================================
    @Test
    void obtenerUsuarioPorId() throws Exception {

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");

        Usuario usuario = new Usuario(
                1L, "Juan", "Pérez", "juan@test.com",
                "123456", "99999999", "Santiago", rol
        );

        when(userService.getById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }


    // ==========================================================
    // TEST CREAR USUARIO
    // ==========================================================
    @Test
    void crearUsuario_retorna201() throws Exception {

        Rol rol = new Rol();
        rol.setId(2L);
        rol.setNombre("CLIENTE");

        Usuario nuevo = new Usuario(
                null, "Ana", "López", "ana@test.com",
                "clave123", "88888888", "Valparaíso", rol
        );

        Usuario creado = new Usuario(
                2L, "Ana", "López", "ana@test.com",
                "clave123", "88888888", "Valparaíso", rol
        );

        when(userService.create(any(Usuario.class))).thenReturn(creado);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }


    // ==========================================================
    // TEST ACTUALIZAR USUARIO
    // ==========================================================
    @Test
    void actualizarUsuario_retorna200() throws Exception {

        Rol rol = new Rol();
        rol.setId(2L);
        rol.setNombre("CLIENTE");

        Usuario updateData = new Usuario(
                1L, "Pedro", "Gómez", "pedro@test.com",
                "nuevaClave", "77777777", "Maipú", rol
        );

        when(userService.update(eq(1L), any(Usuario.class))).thenReturn(updateData);

        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }


    // ==========================================================
    // TEST ELIMINAR USUARIO
    // ==========================================================
    @Test
    void eliminarUsuario_retorna204() throws Exception {

        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }


    // ==========================================================
    // TEST LOGIN
    // ==========================================================
    @Test
    void loginUsuario_retorna200() throws Exception {

        Rol rol = new Rol();
        rol.setId(2L);
        rol.setNombre("CLIENTE");

        Usuario usuario = new Usuario(
                1L, "Laura", "Test", "laura@test.com",
                "123456", "55555555", "Ñuñoa", rol
        );

        when(userService.login("laura@test.com", "123456"))
                .thenReturn(usuario);

        mockMvc.perform(post("/usuarios/login")
                .param("email", "laura@test.com")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("laura@test.com"));
    }


    // ==========================================================
    // TEST CAMBIAR ROL
    // ==========================================================
    @Test
    void cambiarRol_retorna200() throws Exception {

        Rol rolNuevo = new Rol();
        rolNuevo.setId(1L);
        rolNuevo.setNombre("ADMIN");

        Usuario usuarioActualizado = new Usuario(
                1L, "Juan", "Pérez", "juan@test.com",
                "123456", "99999999", "Santiago", rolNuevo
        );

        when(userService.cambiarRol(1L, 1L)).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuarios/1/rol/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol.nombre").value("ADMIN"));
    }
}
