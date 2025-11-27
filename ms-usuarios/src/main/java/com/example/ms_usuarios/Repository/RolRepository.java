package com.example.ms_usuarios.Repository;

import java.util.Optional;
import com.example.ms_usuarios.Model.Rol;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    
}
