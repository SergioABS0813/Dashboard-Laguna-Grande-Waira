package pe.grande.laguna.dashboard.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "notification")
public class Notification implements Serializable {
    private String id;
    private Date timeCreation;
    private String description;
    private String type; // "BATERIA BAJA", "MEDIDOR INOPERATIVO", "SOBRECARGA", "CORTE DE SERVICIO"
    private String idMicronetwork;
    private String nameMicronetwork;

    public String getIdMicronetwork() {
        return idMicronetwork;
    }

    public void setIdMicronetwork(String idMicronetwork) {
        this.idMicronetwork = idMicronetwork;
    }

    public Notification(String id, Date timeCreation, String description, String type, String idMicronetwork) {
        this.id = id;
        this.timeCreation = timeCreation;
        this.description = description;
        this.type = type;
        this.idMicronetwork = idMicronetwork;
    }

    public String getNameMicronetwork() {
        return nameMicronetwork;
    }

    public void setNameMicronetwork(String nameMicronetwork) {
        this.nameMicronetwork = nameMicronetwork;
    }

    public Notification() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Date timeCreation) {
        this.timeCreation = timeCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
