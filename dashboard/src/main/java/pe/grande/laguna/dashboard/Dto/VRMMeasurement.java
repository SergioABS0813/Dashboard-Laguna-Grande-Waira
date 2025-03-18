package pe.grande.laguna.dashboard.Dto;

public class VRMMeasurement {
    private Integer siteId;
    private Double voltage;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public VRMMeasurement(Integer siteId, Double voltage) {
        this.siteId = siteId;
        this.voltage = voltage;
    }

    public VRMMeasurement() {

    }


}
