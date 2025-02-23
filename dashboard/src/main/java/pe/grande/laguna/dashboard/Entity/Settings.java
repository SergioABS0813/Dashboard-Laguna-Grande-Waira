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
    @Field(value = "tokenWeatherLink")
    private String tokenWeatherLink;
    @Field(value = "tokenSparkMeter")
    private String tokenSparkMeter;

    @Field(value = "alertsEmail")
    private boolean alertsEmail;
    @Field(value = "alertsSMS")
    private boolean alertsSMS;
    @Field(value = "alertsWhatsapp")
    private boolean alertsWhatsapp;

    @Field(value = "timeEdition")
    private Date timeEdition;

    public Settings() {
    }

    public Settings(@NonNull String id, String adminId, String tokenVRM, String tokenWeatherLink, String tokenSparkMeter, boolean alertsEmail, boolean alertsSMS, boolean alertsWhatsapp, Date timeEdition) {
        this.id = id;
        this.adminId = adminId;
        this.tokenVRM = tokenVRM;
        this.tokenWeatherLink = tokenWeatherLink;
        this.tokenSparkMeter = tokenSparkMeter;
        this.alertsEmail = alertsEmail;
        this.alertsSMS = alertsSMS;
        this.alertsWhatsapp = alertsWhatsapp;
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

    public boolean isAlertsEmail() {
        return alertsEmail;
    }

    public void setAlertsEmail(boolean alertsEmail) {
        this.alertsEmail = alertsEmail;
    }

    public boolean isAlertsSMS() {
        return alertsSMS;
    }

    public void setAlertsSMS(boolean alertsSMS) {
        this.alertsSMS = alertsSMS;
    }

    public boolean isAlertsWhatsapp() {
        return alertsWhatsapp;
    }

    public void setAlertsWhatsapp(boolean alertsWhatsapp) {
        this.alertsWhatsapp = alertsWhatsapp;
    }

    public Date getTimeEdition() {
        return timeEdition;
    }

    public void setTimeEdition(Date timeEdition) {
        this.timeEdition = timeEdition;
    }
}
