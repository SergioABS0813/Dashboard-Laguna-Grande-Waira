package pe.grande.laguna.dashboard.Dto.ResponsesJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkmeterCustomerResponse {

    private List<String> errors;
    private DataDTO data;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDTO {
        private String name;
        private String code;
        private String phone_number;
        private String id;
        private Balances balances;
        private List<Meter> meters;
        private String last_plan_renewal;
        private String next_plan_renewal;
        private String service_area_id;
        private String site_id;
        private String last_energy_limit_reset_at;
        private Double last_energy_limit_reset_energy;
        private boolean energy_limited;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Balances getBalances() {
            return balances;
        }

        public void setBalances(Balances balances) {
            this.balances = balances;
        }

        public List<Meter> getMeters() {
            return meters;
        }

        public void setMeters(List<Meter> meters) {
            this.meters = meters;
        }

        public String getLast_plan_renewal() {
            return last_plan_renewal;
        }

        public void setLast_plan_renewal(String last_plan_renewal) {
            this.last_plan_renewal = last_plan_renewal;
        }

        public String getNext_plan_renewal() {
            return next_plan_renewal;
        }

        public void setNext_plan_renewal(String next_plan_renewal) {
            this.next_plan_renewal = next_plan_renewal;
        }

        public String getService_area_id() {
            return service_area_id;
        }

        public void setService_area_id(String service_area_id) {
            this.service_area_id = service_area_id;
        }

        public String getSite_id() {
            return site_id;
        }

        public void setSite_id(String site_id) {
            this.site_id = site_id;
        }

        public String getLast_energy_limit_reset_at() {
            return last_energy_limit_reset_at;
        }

        public void setLast_energy_limit_reset_at(String last_energy_limit_reset_at) {
            this.last_energy_limit_reset_at = last_energy_limit_reset_at;
        }

        public Double getLast_energy_limit_reset_energy() {
            return last_energy_limit_reset_energy;
        }

        public void setLast_energy_limit_reset_energy(Double last_energy_limit_reset_energy) {
            this.last_energy_limit_reset_energy = last_energy_limit_reset_energy;
        }

        public boolean isEnergy_limited() {
            return energy_limited;
        }

        public void setEnergy_limited(boolean energy_limited) {
            this.energy_limited = energy_limited;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balances {
        private BalanceItem credit;
        private BalanceItem plan;
        private BalanceItem technical_debt;

        public BalanceItem getCredit() {
            return credit;
        }

        public void setCredit(BalanceItem credit) {
            this.credit = credit;
        }

        public BalanceItem getPlan() {
            return plan;
        }

        public void setPlan(BalanceItem plan) {
            this.plan = plan;
        }

        public BalanceItem getTechnical_debt() {
            return technical_debt;
        }

        public void setTechnical_debt(BalanceItem technical_debt) {
            this.technical_debt = technical_debt;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BalanceItem {
        private String value;
        private String currency;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meter {
        private String address;
        private String coordinates;
        private String operating_mode;
        private String tariff_id;
        private String pole_id;
        private String meter_phase;
        private Tariff tariff;
        private String id;
        private String serial;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(String coordinates) {
            this.coordinates = coordinates;
        }

        public String getOperating_mode() {
            return operating_mode;
        }

        public void setOperating_mode(String operating_mode) {
            this.operating_mode = operating_mode;
        }

        public String getTariff_id() {
            return tariff_id;
        }

        public void setTariff_id(String tariff_id) {
            this.tariff_id = tariff_id;
        }

        public String getPole_id() {
            return pole_id;
        }

        public void setPole_id(String pole_id) {
            this.pole_id = pole_id;
        }

        public String getMeter_phase() {
            return meter_phase;
        }

        public void setMeter_phase(String meter_phase) {
            this.meter_phase = meter_phase;
        }

        public Tariff getTariff() {
            return tariff;
        }

        public void setTariff(Tariff tariff) {
            this.tariff = tariff;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tariff {
        private String id;
        private String name;
        private String electricity_rate_type;
        private Object block_rate;
        private RateAmount rate_amount;
        private Object time_of_use;
        private String load_limit_type;
        private LoadLimit load_limit;
        private String plan_type;
        private LowBalanceThreshold low_balance_threshold;
        private boolean inrush_current_protection_disabled;
        private Object block_rate_cycle_reset_energy;
        private Object last_block_rate_cycle_reset_at;
        private Object daily_energy_limit;
        private String daily_energy_limit_unit;
        private Integer start_day;
        private FixedFee fixed_fee;
        private FixedFee minimum_spend;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getElectricity_rate_type() {
            return electricity_rate_type;
        }

        public void setElectricity_rate_type(String electricity_rate_type) {
            this.electricity_rate_type = electricity_rate_type;
        }

        public Object getBlock_rate() {
            return block_rate;
        }

        public void setBlock_rate(Object block_rate) {
            this.block_rate = block_rate;
        }

        public RateAmount getRate_amount() {
            return rate_amount;
        }

        public void setRate_amount(RateAmount rate_amount) {
            this.rate_amount = rate_amount;
        }

        public Object getTime_of_use() {
            return time_of_use;
        }

        public void setTime_of_use(Object time_of_use) {
            this.time_of_use = time_of_use;
        }

        public String getLoad_limit_type() {
            return load_limit_type;
        }

        public void setLoad_limit_type(String load_limit_type) {
            this.load_limit_type = load_limit_type;
        }

        public LoadLimit getLoad_limit() {
            return load_limit;
        }

        public void setLoad_limit(LoadLimit load_limit) {
            this.load_limit = load_limit;
        }

        public String getPlan_type() {
            return plan_type;
        }

        public void setPlan_type(String plan_type) {
            this.plan_type = plan_type;
        }

        public LowBalanceThreshold getLow_balance_threshold() {
            return low_balance_threshold;
        }

        public void setLow_balance_threshold(LowBalanceThreshold low_balance_threshold) {
            this.low_balance_threshold = low_balance_threshold;
        }

        public boolean isInrush_current_protection_disabled() {
            return inrush_current_protection_disabled;
        }

        public void setInrush_current_protection_disabled(boolean inrush_current_protection_disabled) {
            this.inrush_current_protection_disabled = inrush_current_protection_disabled;
        }

        public Object getBlock_rate_cycle_reset_energy() {
            return block_rate_cycle_reset_energy;
        }

        public void setBlock_rate_cycle_reset_energy(Object block_rate_cycle_reset_energy) {
            this.block_rate_cycle_reset_energy = block_rate_cycle_reset_energy;
        }

        public Object getLast_block_rate_cycle_reset_at() {
            return last_block_rate_cycle_reset_at;
        }

        public void setLast_block_rate_cycle_reset_at(Object last_block_rate_cycle_reset_at) {
            this.last_block_rate_cycle_reset_at = last_block_rate_cycle_reset_at;
        }

        public Object getDaily_energy_limit() {
            return daily_energy_limit;
        }

        public void setDaily_energy_limit(Object daily_energy_limit) {
            this.daily_energy_limit = daily_energy_limit;
        }

        public String getDaily_energy_limit_unit() {
            return daily_energy_limit_unit;
        }

        public void setDaily_energy_limit_unit(String daily_energy_limit_unit) {
            this.daily_energy_limit_unit = daily_energy_limit_unit;
        }

        public Integer getStart_day() {
            return start_day;
        }

        public void setStart_day(Integer start_day) {
            this.start_day = start_day;
        }

        public FixedFee getFixed_fee() {
            return fixed_fee;
        }

        public void setFixed_fee(FixedFee fixed_fee) {
            this.fixed_fee = fixed_fee;
        }

        public FixedFee getMinimum_spend() {
            return minimum_spend;
        }

        public void setMinimum_spend(FixedFee minimum_spend) {
            this.minimum_spend = minimum_spend;
        }
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RateAmount {
        private String value;
        private String numerator;
        private String denominator;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getNumerator() {
            return numerator;
        }

        public void setNumerator(String numerator) {
            this.numerator = numerator;
        }

        public String getDenominator() {
            return denominator;
        }

        public void setDenominator(String denominator) {
            this.denominator = denominator;
        }
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoadLimit {
        private Integer value;
        private String unit;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LowBalanceThreshold {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FixedFee {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }


}
