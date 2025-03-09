package com.cms.model;

import java.time.LocalDateTime;

public class InsurancePolicy {
    public String policyId;
    public double premium;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public PolicyStatus status;

    public enum PolicyStatus {
        ACTIVE, EXPIRED, CANCELLED
    }

    public InsurancePolicy(String policyId, double premium) {
        this.policyId = policyId;
        this.premium = premium;
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusYears(1);
        this.status = PolicyStatus.ACTIVE;
    }
}