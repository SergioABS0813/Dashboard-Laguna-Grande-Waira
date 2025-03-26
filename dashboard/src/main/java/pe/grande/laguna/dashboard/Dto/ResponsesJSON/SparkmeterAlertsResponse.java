package pe.grande.laguna.dashboard.Dto.ResponsesJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignora campos no mapeados, si la API agrega o quita campos en el futuro, no nos falle la deserialización.
public class SparkmeterAlertsResponse {

    private List<String> errors;
    private List<AlertData> data;
    private String cursor;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<AlertData> getData() {
        return data;
    }

    public void setData(List<AlertData> data) {
        this.data = data;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public static class AlertData {
        private String id;
        private String site_id;
        private String severity;           // "severity": "error"
        private String timestamp;          // "timestamp": "2025-02-19T22:00:01.862512+00:00"
        private String updated_timestamp;  // ...
        private String resolved_timestamp; // ...
        private String type;              // "type": "meter_state_tamper_error"
        private String customer_id;        // "customer_id": "8410b348-..."

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSite_id() {
            return site_id;
        }

        public void setSite_id(String site_id) {
            this.site_id = site_id;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUpdated_timestamp() {
            return updated_timestamp;
        }

        public void setUpdated_timestamp(String updated_timestamp) {
            this.updated_timestamp = updated_timestamp;
        }

        public String getResolved_timestamp() {
            return resolved_timestamp;
        }

        public void setResolved_timestamp(String resolved_timestamp) {
            this.resolved_timestamp = resolved_timestamp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }
    }


}
