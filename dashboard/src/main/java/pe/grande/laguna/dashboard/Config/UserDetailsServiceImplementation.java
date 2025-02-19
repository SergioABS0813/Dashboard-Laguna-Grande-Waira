package pe.grande.laguna.dashboard.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserDetailsServiceImplementation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Buscando usuario por email: " + email);
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("Usuario no encontrado: " + email);
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
        User user = userOptional.get();
        System.out.println("Usuario encontrado: " + user.getEmail() + " " + user.getId());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // Asegúrate de que user.getRole() devuelve algo como "USER" o "ADMIN"
                .build();
    }

}
