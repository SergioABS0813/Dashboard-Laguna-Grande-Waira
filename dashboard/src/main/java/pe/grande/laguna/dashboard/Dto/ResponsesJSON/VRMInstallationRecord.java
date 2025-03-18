package pe.grande.laguna.dashboard.Dto.ResponsesJSON;

public class VRMInstallationRecord {
    private int idSite;
    private String name;
    private boolean owner;
    private boolean is_admin;
    // y demás campos que quieras mapear

    // Getters y setters
    public int getIdSite() {
        return idSite;
    }
    public void setIdSite(int idSite) {
        this.idSite = idSite;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isOwner() {
        return owner;
    }
    public void setOwner(boolean owner) {
        this.owner = owner;
    }
    public boolean isIs_admin() {
        return is_admin;
    }
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
}
