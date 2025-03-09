package com.cms.service;

import com.cms.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CMSService {
    private Map<String, Farmer> farmers;
    private Map<String, Admin> admins;
    private List<AuditLog> auditLogs;
    private List<InsuranceClaim> insuranceClaims;
    private String Admin_pw;
    private String Farmer_pw;
    public CMSService() {
        this.farmers = new HashMap<>();
        this.admins = new HashMap<>();
        this.auditLogs = new ArrayList<>();
        this.insuranceClaims = new ArrayList<>();
        
        Admin defaultAdmin = new Admin("admin", "Default Admin", "System", "admin123");
        admins.put(defaultAdmin.getId(), defaultAdmin);
    }

    public boolean registerFarmer(String id, String name, String farmLocation, String password) {
        if (farmers.containsKey(id)) {
            return false;
        }
        Farmer farmer = new Farmer(id, name, farmLocation, password);
        farmers.put(id, farmer);
        Farmer_pw=farmer.hashPassword(password);
        logAction("Farmer registered: " + id);
        AuditLog.logPasswordView(id,Farmer_pw,FALSE);
        return true;
    }

    public Farmer login(String id, String password) {
        Farmer farmer = farmers.get(id);
        if (farmer != null && farmer.checkPassword(password)) {
            logAction("Farmer logged in: " + id);
            AuditLog.logPasswordView(id,Farmer_pw,FALSE);
            return farmer;
        }
        return null;
    }

    public Admin loginAdmin(String id, String password) {
        Admin admin = admins.get(id);
        if (admin != null && admin.checkPassword(password)) {
            logAction("Admin logged in: " + id );
            AuditLog.logPasswordView(id,Admin_pw,TRUE);
            return admin;
        }
        return null;
    }

    public Cattle registerCattle(String farmerId, String breed, int age, double weight,boolean isVaccinated, String vaccinationDate) {
        Farmer farmer = farmers.get(farmerId);
        if (farmer == null) {
            return null;
        }
        String uid = generateUID();
        Cattle cattle = new Cattle(uid, breed, age, weight,isVaccinated,vaccinationDate);
        farmer.getCattle().add(cattle);
        logAction("Cattle registered: " + uid + " for farmer: " + farmerId);
        return cattle;
    }

    public InsurancePolicy createInsurancePolicy(String cattleUid, double premium) {
        Cattle cattle = findCattleByUid(cattleUid);
        if (cattle == null) {
            return null;
        }
        InsurancePolicy policy = new InsurancePolicy(generatePolicyId(), calculatePremium(cattle));
        cattle.setInsurancePolicy(policy);
        logAction("Insurance policy created for cattle: " + cattleUid);
        return policy;
    }

    public InsuranceClaim fileClaim(String cattleUid, String reason) {
        Cattle cattle = findCattleByUid(cattleUid);
        if (cattle == null) {
            return null;
        }
        
        InsuranceClaim.ClaimReason claimReason;
        String otherReason = null;

        try {
            claimReason = InsuranceClaim.ClaimReason.valueOf(reason.toUpperCase());
        } catch (IllegalArgumentException e) {
            claimReason = InsuranceClaim.ClaimReason.OTHER;
            otherReason = reason;
        }
        
        InsuranceClaim claim = new InsuranceClaim(generateClaimId(), cattle, claimReason, otherReason);
        insuranceClaims.add(claim);
        logAction("Insurance claim filed for cattle: " + cattleUid + ", Reason: " + reason);
        
        if (claim.isAutoApprovable()) {
            claim.setStatus(InsuranceClaim.ClaimStatus.APPROVED);
            logAction("Claim " + claim.getClaimId() + " auto-approved for reason: " + reason);
        }
        
        return claim;
    }

    public List<InsuranceClaim> getPendingClaims() {
        List<InsuranceClaim> pendingClaims = new ArrayList<>();
        for (InsuranceClaim claim : insuranceClaims) {
            if (claim.getStatus() == InsuranceClaim.ClaimStatus.UNDER_REVIEW) {
                pendingClaims.add(claim);
            }
        }
        return pendingClaims;
    }

    public List<AuditLog> getAuditLogs() {
        return (auditLogs);
    }

    public List<InsuranceClaim> getAllInsuranceClaims() {
        return (insuranceClaims);
    }

    public boolean updateClaimStatus(String claimId, InsuranceClaim.ClaimStatus newStatus) {
        for (InsuranceClaim claim : insuranceClaims) {
            if (claim.getClaimId().equals(claimId)) {
                if (claim.getStatus() == InsuranceClaim.ClaimStatus.UNDER_REVIEW) {
                    claim.setStatus(newStatus);
                    logAction("Claim " + claimId + " status updated to " + newStatus);
                    return true;
                } else {
                    logAction("Attempt to update status of non-pending claim " + claimId + " denied");
                    return false;
                }
            }
        }
        return false;
    }

    public boolean registerAdmin(String id, String name, String department, String password) {
        if (admins.containsKey(id)) {
            return false;
        }
        Admin newAdmin = new Admin(id, name, department, password);
        admins.put(id, newAdmin);
        Admin_pw=newAdmin.hashPassword(password);
        logAction("Admin registered: " + id);
        AuditLog.logPasswordView(id,Admin_pw,TRUE);
        return true;
    }

    private void logAction(String action) {
        AuditLog log = new AuditLog(action);
        auditLogs.add(log);
        System.out.println("Log: " + action); 
    }

    public String generateUID() {
        return "CATTLE" + System.currentTimeMillis();
    }

    private String generatePolicyId() {
        return "POL" + System.currentTimeMillis();
    }

    private String generateClaimId() {
        return "CLM" + System.currentTimeMillis();
    }

    public double calculatePremium(Cattle cattle) {
        double basePremium = 1000.0;
        double ageFactor = cattle.getAge() > 5 ? 1.5 : 1.0;
        return basePremium * ageFactor;
    }

    private Cattle findCattleByUid(String uid) {
        for (Farmer farmer : farmers.values()) {
            for (Cattle cattle : farmer.getCattle()) {
                if (cattle.getUid().equals(uid)) {
                    return cattle;
                }
            }
        }
        return null;
    }
}