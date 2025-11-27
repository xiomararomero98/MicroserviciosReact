package com.example.ms_usuarios.Repository;
import com.example.ms_usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

        boolean existsByEmail(String email);

    
}
