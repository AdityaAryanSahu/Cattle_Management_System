package com.model;

import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class InsuranceClaim {
    private String claimId;
    private Cattle cattle;
    private ClaimReason reason;
    private String otherReason;
    private LocalDateTime submissionDate;
    private ClaimStatus status;
    private String checksum;

    public enum ClaimStatus {
        SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED
    }

    public enum ClaimReason {
        STOLEN, ILLNESS, ACCIDENT, NATURAL_DISASTER, OTHER
    }

    private static final List<ClaimReason> AUTO_APPROVE_REASONS = Arrays.asList(
        ClaimReason.STOLEN, ClaimReason.ILLNESS, ClaimReason.ACCIDENT, ClaimReason.NATURAL_DISASTER
    );

    public InsuranceClaim(String claimId, Cattle cattle, ClaimReason reason, String otherReason) {
        this.claimId = claimId;
        this.cattle = cattle;
        this.reason = reason;
        this.otherReason = otherReason;
        this.submissionDate = LocalDateTime.now();
        this.status = (reason == ClaimReason.OTHER) ? ClaimStatus.UNDER_REVIEW : ClaimStatus.SUBMITTED;
        updateChecksum();
    }

    public void updateChecksum() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = claimId + cattle.getUid() + reason + otherReason + submissionDate.toString() + status.toString();
            byte[] hash = digest.digest(data.getBytes());
            this.checksum = bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getClaimId() {
        return claimId;
    }

    public Cattle getCattle() {
        return cattle;
    }

    public ClaimReason getReason() {
        return reason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
        updateChecksum();
    }

    public boolean isAutoApprovable() {
        return AUTO_APPROVE_REASONS.contains(this.reason);
    }
}
