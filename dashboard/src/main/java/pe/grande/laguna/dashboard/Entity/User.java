package pe.grande.laguna.dashboard.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@Document(collection = "users")
public class User implements Serializable {

    @Id
    @NonNull
    private String id;

    @Field(value = "email")
    @NotBlank(message = "El correo es un campo obligatorio")
    private String email;

    @Field(value = "password")
    @NotBlank(message = "La contraseña es un campo obligatorio")
    private String password;

    @Field(value = "role")
    private String role; // ADMIN o USER

    @Field(value = "active")
    private boolean active = true;

    @Field(value = "phone")
    @NotBlank(message = "El número de celular es un campo obligatorio")
    private String phone;

    @Field(value = "name")
    @NotBlank(message = "El nombre de usuario es un campo obligatorio")
    private String name;

    @Field(value = "lastname")
    @NotBlank(message = "El apellido de usuario es un campo obligatorio")
    private String lastname;

    @Field(value = "creationTime")
    private Date creationTime;

    @Field(value = "micronetworkList")
    private ArrayList<String> micronetworkList = new ArrayList<>();

    @Field(value = "alertsEmail")
    private boolean alertsEmail;

    @Field(value = "alertsSMS")
    private boolean alertsSMS;

    @Field(value = "alertsWhatsapp")
    private boolean alertsWhatsapp;

    public User() {}

    public User(@NonNull String id, String email, String password, String role, boolean active, String phone, String name, String lastname, Date creationTime, ArrayList<String> micronetworkList, boolean alertsEmail, boolean alertsSMS, boolean alertsWhatsapp) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.phone = phone;
        this.name = name;
        this.lastname = lastname;
        this.creationTime = creationTime;
        this.micronetworkList = micronetworkList;
        this.alertsEmail = alertsEmail;
        this.alertsSMS = alertsSMS;
        this.alertsWhatsapp = alertsWhatsapp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getMicronetworkList() {
        return micronetworkList;
    }

    public void setMicronetworkList(ArrayList<String> micronetworkList) {
        this.micronetworkList = micronetworkList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
}
