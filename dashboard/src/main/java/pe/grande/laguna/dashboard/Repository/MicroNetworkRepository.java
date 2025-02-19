package pe.grande.laguna.dashboard.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;

public interface MicroNetworkRepository extends MongoRepository<MicroNetwork, String> {



}
