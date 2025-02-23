package pe.grande.laguna.dashboard.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pe.grande.laguna.dashboard.Entity.Settings;

@Repository
public interface SettingsRepository extends MongoRepository<Settings, String> {

    Settings findByAdminId(String id);
}
