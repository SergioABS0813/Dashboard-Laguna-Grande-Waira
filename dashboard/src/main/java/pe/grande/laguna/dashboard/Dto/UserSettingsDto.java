package pe.grande.laguna.dashboard.Dto;

import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;

public class UserSettingsDto {

    private User user;
    private Settings settings;

    // Constructor vacío
    public UserSettingsDto() {
    }

    // Constructor con parámetros
    public UserSettingsDto(User user, Settings settings) {
        this.user = user;
        this.settings = settings;
    }

    // Getters y Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
