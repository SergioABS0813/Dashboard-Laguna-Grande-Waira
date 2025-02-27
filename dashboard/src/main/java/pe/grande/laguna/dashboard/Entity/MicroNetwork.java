package pe.grande.laguna.dashboard.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "El site de VRM es obligatorio")
    @Field(value = "siteVRM")
    private Integer siteVRM;

    @NotNull(message = "El site de Weather Link es obligatorio")
    @Field(value = "siteWeatherLink")
    private Integer siteWeatherLink;

    @NotBlank(message = "El site de Spark Meter es obligatorio")
    @Field(value = "siteSparkMeter")
    private String siteSparkMeter;

    @NotBlank(message = "El alias es obligatorio")
    @Field(value = "alias")
    private String alias;

    @Pattern(regexp = "^-?[0-9]{1,3}(\\.[0-9]+)?$", message = "La latitud debe ser un número válido. Por favor, ingrese un valor")
    @Field(value = "lat")
    private String lat;

    @Pattern(regexp = "^-?[0-9]{1,3}(\\.[0-9]+)?$", message = "La longitud debe ser un número válido. Por favor, ingrese un valor")
    @Field(value = "lon")
    private String lon;

    @Field(value = "timeCreation")
    private Date timeCreation;
    @Field(value = "timeEdition")
    private Date timeEdition;

    @Field(value = "status")
    private String status;

    @NotBlank(message = "La provincia es obligatoria.")
    @Field(value = "provincia")
    private String provincia;

    @NotBlank(message = "El departamento es obligatorio.")
    @Field(value = "departamento")
    private String departamento;

    public MicroNetwork() {
    }

    public MicroNetwork(@NonNull String id, Integer siteVRM, Integer siteWeatherLink, String siteSparkMeter, String alias, String lat, String lon, Date timeCreation, Date timeEdition, String status, String provincia, String departamento) {
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

    public Integer getSiteVRM() {
        return siteVRM;
    }

    public void setSiteVRM(Integer siteVRM) {
        this.siteVRM = siteVRM;
    }

    public Integer getSiteWeatherLink() {
        return siteWeatherLink;
    }

    public void setSiteWeatherLink(Integer siteWeatherLink) {
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
