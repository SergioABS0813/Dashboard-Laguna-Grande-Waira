package pe.grande.laguna.dashboard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.grande.laguna.dashboard.Dto.SPCustomerDTO;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.SparkMeterService;
import pe.grande.laguna.dashboard.Service.WeatherLinkService;

import java.util.List;
import java.util.Map;

@RestController
public class ApiControllerSparkMeter {

    private final SparkMeterService sparkMeterService;
    private final SettingsRepository settingsRepository;

    public ApiControllerSparkMeter(SparkMeterService sparkMeterService, SettingsRepository settingsRepository) {
        this.sparkMeterService = sparkMeterService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/api/sparkmeter/get-customers")
    public ResponseEntity<List<SPCustomerDTO>> getMeters(@RequestParam("siteId") String siteId) {

        String sparkMeterKey = obtenerKeySeguro();
        String sparkMeterSecret = obtenerSecretSeguro();

        ResponseEntity<Map> response = sparkMeterService.getCustomersList(siteId, sparkMeterKey, sparkMeterSecret);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            Map<String, Object> responseBody = response.getBody();

            // Extraer la lista de clientes desde la respuesta de la API
            List<Map<String, Object>> rawCustomers = (List<Map<String, Object>>) responseBody.get("data");

            if (rawCustomers == null) {
                return ResponseEntity.ok().body(List.of());
            }

            // Filtrar clientes por serviceAreaId y crédito menor o igual a 10
            List<SPCustomerDTO> filteredCustomers = rawCustomers.stream()
                    .filter(customer -> siteId.equals(customer.get("service_area_id")))
                    .map(this::mapToSPCustomerDTO)
                    .filter(customerDTO -> customerDTO.getBalances().getCredit().getValue() != null &&
                            (Double.parseDouble(customerDTO.getBalances().getCredit().getValue()) + Double.parseDouble(customerDTO.getBalances().getPlan().getValue())) <= 10)
                    .toList();

            return ResponseEntity.ok(filteredCustomers);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }

    private SPCustomerDTO mapToSPCustomerDTO(Map<String, Object> rawCustomer) {
        SPCustomerDTO customerDTO = new SPCustomerDTO();

        customerDTO.setName((String) rawCustomer.get("name"));
        customerDTO.setCode((String) rawCustomer.get("code"));
        customerDTO.setPhoneNumber((String) rawCustomer.get("phone_number"));
        customerDTO.setId((String) rawCustomer.get("id"));
        customerDTO.setServiceAreaId((String) rawCustomer.get("service_area_id"));
        customerDTO.setSiteId((String) rawCustomer.get("site_id"));
        customerDTO.setLastPlanRenewal((String) rawCustomer.get("last_plan_renewal"));
        customerDTO.setNextPlanRenewal((String) rawCustomer.get("next_plan_renewal"));
        customerDTO.setLastEnergyLimitResetAt((String) rawCustomer.get("last_energy_limit_reset_at"));
        customerDTO.setLastEnergyLimitResetEnergy(
                rawCustomer.get("last_energy_limit_reset_energy") != null
                        ? Double.parseDouble(rawCustomer.get("last_energy_limit_reset_energy").toString())
                        : null
        );

        // Validar si existe el campo "energy_limited"
        Object energyLimited = rawCustomer.get("energy_limited");
        if (energyLimited != null) {
            customerDTO.setEnergyLimited(Boolean.parseBoolean(energyLimited.toString()));
        } else {
            customerDTO.setEnergyLimited(false); // Asignar un valor por defecto si no está presente
        }

        // Extraer los balances
        Map<String, Object> balancesMap = (Map<String, Object>) rawCustomer.get("balances");
        if (balancesMap != null) {
            SPCustomerDTO.Balances balances = new SPCustomerDTO.Balances();

            Map<String, Object> creditMap = (Map<String, Object>) balancesMap.get("credit");
            SPCustomerDTO.Balances.Credit credit = new SPCustomerDTO.Balances.Credit();
            if (creditMap != null) {
                credit.setValue((String) creditMap.get("value"));
                credit.setCurrency((String) creditMap.get("currency"));
            }
            balances.setCredit(credit);

            Map<String, Object> planMap = (Map<String, Object>) balancesMap.get("plan");
            SPCustomerDTO.Balances.Plan plan = new SPCustomerDTO.Balances.Plan();
            if (planMap != null) {
                plan.setValue((String) planMap.get("value"));
                plan.setCurrency((String) planMap.get("currency"));
            }
            balances.setPlan(plan);

            Map<String, Object> technicalDebtMap = (Map<String, Object>) balancesMap.get("technical_debt");
            SPCustomerDTO.Balances.TechnicalDebt technicalDebt = new SPCustomerDTO.Balances.TechnicalDebt();
            if (technicalDebtMap != null) {
                technicalDebt.setValue((String) technicalDebtMap.get("value"));
                technicalDebt.setCurrency((String) technicalDebtMap.get("currency"));
            }
            balances.setTechnicalDebt(technicalDebt);

            customerDTO.setBalances(balances);
        }

        // Extraer los meters
        List<Map<String, Object>> metersList = (List<Map<String, Object>>) rawCustomer.get("meters");
        if (metersList != null) {
            List<SPCustomerDTO.Meter> meters = metersList.stream().map(rawMeter -> {
                SPCustomerDTO.Meter meter = new SPCustomerDTO.Meter();
                meter.setId((String) rawMeter.get("id"));
                meter.setSerial((String) rawMeter.get("serial"));
                meter.setOperatingMode((String) rawMeter.get("operating_mode"));

                Map<String, Object> tariffMap = (Map<String, Object>) rawMeter.get("tariff");
                if (tariffMap != null) {
                    SPCustomerDTO.Meter.Tariff tariff = new SPCustomerDTO.Meter.Tariff();
                    tariff.setId((String) tariffMap.get("id"));
                    tariff.setName((String) tariffMap.get("name"));
                    tariff.setElectricityRateType((String) tariffMap.get("electricity_rate_type"));
                    tariff.setPlanType((String) tariffMap.get("plan_type"));

                    // Rate Amount
                    Map<String, Object> rateAmountMap = (Map<String, Object>) tariffMap.get("rate_amount");
                    if (rateAmountMap != null) {
                        SPCustomerDTO.Meter.Tariff.RateAmount rateAmount = new SPCustomerDTO.Meter.Tariff.RateAmount();
                        rateAmount.setValue((String) rateAmountMap.get("value"));
                        rateAmount.setNumerator((String) rateAmountMap.get("numerator"));
                        rateAmount.setDenominator((String) rateAmountMap.get("denominator"));
                        tariff.setRateAmount(rateAmount);
                    }

                    // Load Limit
                    Map<String, Object> loadLimitMap = (Map<String, Object>) tariffMap.get("load_limit");
                    if (loadLimitMap != null) {
                        SPCustomerDTO.Meter.Tariff.LoadLimit loadLimit = new SPCustomerDTO.Meter.Tariff.LoadLimit();
                        loadLimit.setValue((Integer) loadLimitMap.get("value"));
                        loadLimit.setUnit((String) loadLimitMap.get("unit"));
                        tariff.setLoadLimit(loadLimit);
                    }

                    // Fixed Fee
                    Map<String, Object> fixedFeeMap = (Map<String, Object>) tariffMap.get("fixed_fee");
                    if (fixedFeeMap != null) {
                        SPCustomerDTO.Meter.Tariff.FixedFee fixedFee = new SPCustomerDTO.Meter.Tariff.FixedFee();
                        fixedFee.setAmount((String) fixedFeeMap.get("amount"));
                        fixedFee.setCurrency((String) fixedFeeMap.get("currency"));
                        tariff.setFixedFee(fixedFee);
                    }

                    // Minimum Spend
                    Map<String, Object> minimumSpendMap = (Map<String, Object>) tariffMap.get("minimum_spend");
                    if (minimumSpendMap != null) {
                        SPCustomerDTO.Meter.Tariff.MinimumSpend minimumSpend = new SPCustomerDTO.Meter.Tariff.MinimumSpend();
                        minimumSpend.setAmount((String) minimumSpendMap.get("amount"));
                        minimumSpend.setCurrency((String) minimumSpendMap.get("currency"));
                        tariff.setMinimumSpend(minimumSpend);
                    }

                    meter.setTariff(tariff);
                }

                return meter;
            }).toList();

            customerDTO.setMeters(meters);
        }

        return customerDTO;
    }


    private String obtenerKeySeguro() {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getKeySparkMeter();
    }

    private String obtenerSecretSeguro() {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getSecretSparkMeter();
    }


}
