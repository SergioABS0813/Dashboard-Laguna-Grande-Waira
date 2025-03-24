package pe.grande.laguna.dashboard.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;

import java.util.Optional;

public interface MicroNetworkRepository extends MongoRepository<MicroNetwork, String> {

    // Extraer el micronetwork que tenga un siteVRM en específico
    Optional<MicroNetwork> findBySiteVRM(Integer siteVRM);




}
