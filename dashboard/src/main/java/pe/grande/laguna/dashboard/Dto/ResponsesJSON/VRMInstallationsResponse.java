package pe.grande.laguna.dashboard.Dto.ResponsesJSON;

import java.util.List;

public class VRMInstallationsResponse {
    private boolean success;
    private List<VRMInstallationRecord> records;

    // Getters y setters
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public List<VRMInstallationRecord> getRecords() {
        return records;
    }
    public void setRecords(List<VRMInstallationRecord> records) {
        this.records = records;
    }
}
