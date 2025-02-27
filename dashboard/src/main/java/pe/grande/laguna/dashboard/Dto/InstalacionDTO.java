package pe.grande.laguna.dashboard.Dto;

public class InstalacionDTO {
    private String nombre;
    private int idSite;

    public InstalacionDTO(String nombre, int idSite) {
        this.nombre = nombre;
        this.idSite = idSite;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdSite() { return idSite; }
    public void setIdSite(int idSite) { this.idSite = idSite; }
}
