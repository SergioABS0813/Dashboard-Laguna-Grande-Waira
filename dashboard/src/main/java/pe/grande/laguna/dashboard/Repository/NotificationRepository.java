package pe.grande.laguna.dashboard.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pe.grande.laguna.dashboard.Entity.Notification;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    //Consulta para encontrar todas las notificaciones en base al idMicronetwork asociado
    List<Notification> findByIdMicronetwork(String idMicronetwork);


}
