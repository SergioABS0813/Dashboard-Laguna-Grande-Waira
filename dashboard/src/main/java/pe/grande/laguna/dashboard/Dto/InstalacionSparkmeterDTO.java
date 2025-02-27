package pe.grande.laguna.dashboard.Dto;

public class InstalacionSparkmeterDTO {

    private String nombre;
    private String idSite;

    public InstalacionSparkmeterDTO(String nombre, String idSite) {
        this.nombre = nombre;
        this.idSite = idSite;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdSite() {
        return idSite;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }
}
