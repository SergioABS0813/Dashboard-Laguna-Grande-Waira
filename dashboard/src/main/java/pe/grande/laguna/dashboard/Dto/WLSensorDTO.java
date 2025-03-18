package pe.grande.laguna.dashboard.Dto;

import java.util.Map;

public class WLSensorDTO {

    private int lsid;
    private int sensorType;
    private int dataStructureType;
    private String manufacturer;
    private String productName;
    private String productNumber;
    private String category;
    private String dataStructureDescription;
    private Map<String, Object> latestData;

    public WLSensorDTO(int lsid, int sensorType, int dataStructureType, String manufacturer, String productName, String productNumber, String category, String dataStructureDescription, Map<String, Object> latestData) {
        this.lsid = lsid;
        this.sensorType = sensorType;
        this.dataStructureType = dataStructureType;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.productNumber = productNumber;
        this.category = category;
        this.dataStructureDescription = dataStructureDescription;
        this.latestData = latestData;
    }

    public WLSensorDTO() {
    }

    public int getLsid() {
        return lsid;
    }

    public void setLsid(int lsid) {
        this.lsid = lsid;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public int getDataStructureType() {
        return dataStructureType;
    }

    public void setDataStructureType(int dataStructureType) {
        this.dataStructureType = dataStructureType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDataStructureDescription() {
        return dataStructureDescription;
    }

    public void setDataStructureDescription(String dataStructureDescription) {
        this.dataStructureDescription = dataStructureDescription;
    }

    public Map<String, Object> getLatestData() {
        return latestData;
    }

    public void setLatestData(Map<String, Object> latestData) {
        this.latestData = latestData;
    }
}
