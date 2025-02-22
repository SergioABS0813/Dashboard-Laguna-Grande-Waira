package pe.grande.laguna.dashboard.Entity;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field(value = "tokenWeatherLink")
    private String tokenWeatherLink;
    @Field(value = "tokenSparkMeter")
    private String tokenSparkMeter;

    public Settings() {
    }

    public Settings(@NonNull String id, String adminId, String tokenVRM, String tokenWeatherLink, String tokenSparkMeter) {
        this.id = id;
        this.adminId = adminId;
        this.tokenVRM = tokenVRM;
        this.tokenWeatherLink = tokenWeatherLink;
        this.tokenSparkMeter = tokenSparkMeter;
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

    public String getTokenWeatherLink() {
        return tokenWeatherLink;
    }

    public void setTokenWeatherLink(String tokenWeatherLink) {
        this.tokenWeatherLink = tokenWeatherLink;
    }

    public String getTokenSparkMeter() {
        return tokenSparkMeter;
    }

    public void setTokenSparkMeter(String tokenSparkMeter) {
        this.tokenSparkMeter = tokenSparkMeter;
    }
}
