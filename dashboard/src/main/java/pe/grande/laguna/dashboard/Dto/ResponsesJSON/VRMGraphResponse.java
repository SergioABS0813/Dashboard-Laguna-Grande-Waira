package pe.grande.laguna.dashboard.Dto.ResponsesJSON;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VRMGraphResponse {

    private boolean success;
    private Records records;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }

    @Data
    public static class Records {
        private Map<String, List<List<Double>>> data;
        private Map<String, Meta> meta;
        private int instance;

        // --- GETTER MANUAL ---
        public Map<String, List<List<Double>>> getData() {
            return data;
        }

        public Map<String, Meta> getMeta() {
            return meta;
        }

        public int getInstance() {
            return instance;
        }

    }


    @Data
    public static class Meta {
        private String code;
        private String description;
        private String formatValueOnly;
        private String formatWithUnit;
        private String axisGroup;
    }

    public VRMGraphResponse(boolean success, Records records) {
        this.success = success;
        this.records = records;
    }
    public VRMGraphResponse() {

    }
}

