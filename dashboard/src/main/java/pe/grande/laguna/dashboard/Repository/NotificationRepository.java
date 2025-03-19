package pe.grande.laguna.dashboard.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pe.grande.laguna.dashboard.Entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {


}
