package pe.grande.laguna.dashboard.Dto;


public class VRMDeviceDTO {

    private String machineSerialNumber;
    private String name;
    private String productName;
    private String firmwareVersion;
    private Integer idDeviceType;
    private Integer instance;
    private Integer idSite;


    public VRMDeviceDTO(String machineSerialNumber, String name, String productName, String firmwareVersion, Integer idDeviceType, Integer instance, Integer idSite) {
        this.machineSerialNumber = machineSerialNumber;
        this.name = name;
        this.productName = productName;
        this.firmwareVersion = firmwareVersion;
        this.idDeviceType = idDeviceType;
        this.instance = instance;
        this.idSite = idSite;
    }

    public VRMDeviceDTO() {
    }

    public String getMachineSerialNumber() {
        return machineSerialNumber;
    }

    public void setMachineSerialNumber(String machineSerialNumber) {
        this.machineSerialNumber = machineSerialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Integer getIdDeviceType() {
        return idDeviceType;
    }

    public void setIdDeviceType(Integer idDeviceType) {
        this.idDeviceType = idDeviceType;
    }

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    public Integer getIdSite() {
        return idSite;
    }

    public void setIdSite(Integer idSite) {
        this.idSite = idSite;
    }
}
