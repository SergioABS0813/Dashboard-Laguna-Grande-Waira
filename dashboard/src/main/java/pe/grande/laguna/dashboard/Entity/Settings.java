package pe.grande.laguna.dashboard.Entity;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Document(collection = "settings")
public class Settings {

    @Id
    @NonNull
    private String id;

    @Field(value = "adminId")
    private String adminId;

    @Field(value = "tokenVRM")
    private String tokenVRM;

    @Field(value = "keyWeatherLink")
    private String keyWeatherLink;

    @Field(value = "secretWeatherLink")
    private String secretWeatherLink;

    @Field(value = "keySparkMeter")
    private String keySparkMeter;

    @Field(value = "secretSparkMeter")
    private String secretSparkMeter;

    @Field(value = "timeEdition")
    private Date timeEdition;

    public Settings() {
    }

    public Settings(@NonNull String id, String adminId, String tokenVRM, String keyWeatherLink, String secretWeatherLink, String keySparkMeter, String secretSparkMeter, Date timeEdition) {
        this.id = id;
        this.adminId = adminId;
        this.tokenVRM = tokenVRM;
        this.keyWeatherLink = keyWeatherLink;
        this.secretWeatherLink = secretWeatherLink;
        this.keySparkMeter = keySparkMeter;
        this.secretSparkMeter = secretSparkMeter;
        this.timeEdition = timeEdition;
    }

    public @NonNull String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTokenVRM() {
        return tokenVRM;
    }

    public void setTokenVRM(String tokenVRM) {
        this.tokenVRM = tokenVRM;
    }

    public String getKeyWeatherLink() {
        return keyWeatherLink;
    }

    public void setKeyWeatherLink(String keyWeatherLink) {
        this.keyWeatherLink = keyWeatherLink;
    }

    public String getSecretWeatherLink() {
        return secretWeatherLink;
    }

    public void setSecretWeatherLink(String secretWeatherLink) {
        this.secretWeatherLink = secretWeatherLink;
    }

    public String getKeySparkMeter() {
        return keySparkMeter;
    }

    public void setKeySparkMeter(String keySparkMeter) {
        this.keySparkMeter = keySparkMeter;
    }

    public String getSecretSparkMeter() {
        return secretSparkMeter;
    }

    public void setSecretSparkMeter(String secretSparkMeter) {
        this.secretSparkMeter = secretSparkMeter;
    }

    public Date getTimeEdition() {
        return timeEdition;
    }

    public void setTimeEdition(Date timeEdition) {
        this.timeEdition = timeEdition;
    }
}
