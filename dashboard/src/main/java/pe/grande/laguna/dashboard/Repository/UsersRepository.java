package pe.grande.laguna.dashboard.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pe.grande.laguna.dashboard.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    // Buscar un usuario por email
    Optional<User> findByEmail(String email);

    // Buscar todos los usuarios con un rol específico
    List<User> findByRole(String role);

    // Verificar si un usuario con un email específico existe
    boolean existsByEmail(String email);

}
