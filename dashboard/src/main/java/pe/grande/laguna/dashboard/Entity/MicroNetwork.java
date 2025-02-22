package pe.grande.laguna.dashboard.Entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Document(collection = "micronetwork")
public class MicroNetwork implements Serializable {

    @Id
    @NonNull
    private String id;

    @Field(value = "siteVRM")
    private String siteVRM;
    @Field(value = "siteWeatherLink")
    private String siteWeatherLink;
    @Field(value = "siteSparkMeter")
    private String siteSparkMeter;

    @Field(value = "alias")
    private String alias;
    @Field(value = "lat")
    private String lat;
    @Field(value = "lon")
    private String lon;

    @Field(value = "timeCreation")
    private Date timeCreation;
    @Field(value = "timeEdition")
    private Date timeEdition;

    @Field(value = "status")
    private String status;

    @Field(value = "provincia")
    private String provincia;

    @Field(value = "departamento")
    private String departamento;

    public MicroNetwork() {
    }

    public MicroNetwork(@NonNull String id, String siteVRM, String siteWeatherLink, String siteSparkMeter, String alias, String lat, String lon, Date timeCreation, Date timeEdition, String status, String provincia, String departamento) {
        this.id = id;
        this.siteVRM = siteVRM;
        this.siteWeatherLink = siteWeatherLink;
        this.siteSparkMeter = siteSparkMeter;
        this.alias = alias;
        this.lat = lat;
        this.lon = lon;
        this.timeCreation = timeCreation;
        this.timeEdition = timeEdition;
        this.status = status;
        this.provincia = provincia;
        this.departamento = departamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteVRM() {
        return siteVRM;
    }

    public void setSiteVRM(String siteVRM) {
        this.siteVRM = siteVRM;
    }

    public String getSiteWeatherLink() {
        return siteWeatherLink;
    }

    public void setSiteWeatherLink(String siteWeatherLink) {
        this.siteWeatherLink = siteWeatherLink;
    }

    public String getSiteSparkMeter() {
        return siteSparkMeter;
    }

    public void setSiteSparkMeter(String siteSparkMeter) {
        this.siteSparkMeter = siteSparkMeter;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Date getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Date timeCreation) {
        this.timeCreation = timeCreation;
    }

    public Date getTimeEdition() {
        return timeEdition;
    }

    public void setTimeEdition(Date timeEdition) {
        this.timeEdition = timeEdition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
