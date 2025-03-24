package pe.grande.laguna.dashboard.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SPCustomerDTO {

    private String name;
    private String code;
    private String phoneNumber;
    private String id;
    private Balances balances;
    private List<Meter> meters;
    private String lastPlanRenewal;
    private String nextPlanRenewal;
    private String serviceAreaId;
    private String siteId;
    private String lastEnergyLimitResetAt;
    private Double lastEnergyLimitResetEnergy;
    private boolean energyLimited;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getLastPlanRenewal() {
        return lastPlanRenewal;
    }

    public void setLastPlanRenewal(String lastPlanRenewal) {
        this.lastPlanRenewal = lastPlanRenewal;
    }

    public String getNextPlanRenewal() {
        return nextPlanRenewal;
    }

    public void setNextPlanRenewal(String nextPlanRenewal) {
        this.nextPlanRenewal = nextPlanRenewal;
    }

    public String getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(String serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLastEnergyLimitResetAt() {
        return lastEnergyLimitResetAt;
    }

    public void setLastEnergyLimitResetAt(String lastEnergyLimitResetAt) {
        this.lastEnergyLimitResetAt = lastEnergyLimitResetAt;
    }

    public Double getLastEnergyLimitResetEnergy() {
        return lastEnergyLimitResetEnergy;
    }

    public void setLastEnergyLimitResetEnergy(Double lastEnergyLimitResetEnergy) {
        this.lastEnergyLimitResetEnergy = lastEnergyLimitResetEnergy;
    }

    public boolean isEnergyLimited() {
        return energyLimited;
    }

    public void setEnergyLimited(boolean energyLimited) {
        this.energyLimited = energyLimited;
    }

    public static class Balances {
        private Credit credit;
        private Plan plan;
        private TechnicalDebt technicalDebt;

        public Credit getCredit() {
            return credit;
        }

        public void setCredit(Credit credit) {
            this.credit = credit;
        }

        public Plan getPlan() {
            return plan;
        }

        public void setPlan(Plan plan) {
            this.plan = plan;
        }

        public TechnicalDebt getTechnicalDebt() {
            return technicalDebt;
        }

        public void setTechnicalDebt(TechnicalDebt technicalDebt) {
            this.technicalDebt = technicalDebt;
        }

        public static class Credit {
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


        public static class Plan {
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


        public static class TechnicalDebt {
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


    }

    public static class Meter {
        private String id;
        private String serial;
        private String operatingMode;
        private Tariff tariff;

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

        public String getOperatingMode() {
            return operatingMode;
        }

        public void setOperatingMode(String operatingMode) {
            this.operatingMode = operatingMode;
        }

        public Tariff getTariff() {
            return tariff;
        }

        public void setTariff(Tariff tariff) {
            this.tariff = tariff;
        }

        public static class Tariff {
            private String id;
            private String name;
            private String electricityRateType;
            private String planType;
            private RateAmount rateAmount;
            private LoadLimit loadLimit;
            private FixedFee fixedFee;
            private MinimumSpend minimumSpend;

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

            public String getElectricityRateType() {
                return electricityRateType;
            }

            public void setElectricityRateType(String electricityRateType) {
                this.electricityRateType = electricityRateType;
            }

            public String getPlanType() {
                return planType;
            }

            public void setPlanType(String planType) {
                this.planType = planType;
            }

            public RateAmount getRateAmount() {
                return rateAmount;
            }

            public void setRateAmount(RateAmount rateAmount) {
                this.rateAmount = rateAmount;
            }

            public LoadLimit getLoadLimit() {
                return loadLimit;
            }

            public void setLoadLimit(LoadLimit loadLimit) {
                this.loadLimit = loadLimit;
            }

            public FixedFee getFixedFee() {
                return fixedFee;
            }

            public void setFixedFee(FixedFee fixedFee) {
                this.fixedFee = fixedFee;
            }

            public MinimumSpend getMinimumSpend() {
                return minimumSpend;
            }

            public void setMinimumSpend(MinimumSpend minimumSpend) {
                this.minimumSpend = minimumSpend;
            }

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

            public static class MinimumSpend {
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

    }
}

