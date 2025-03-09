package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private String action;
    private LocalDateTime timestamp;
    private static List<AuditLog> logs = new ArrayList<>();
    private String additionalInfo;
    private String passwordInfo;

    public AuditLog(String action) {
        this(action, null, null);
    }

    public AuditLog(String action, String additionalInfo) {
        this(action, additionalInfo, null);
    }

    public AuditLog(String action, String additionalInfo, String passwordInfo) {
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.additionalInfo = additionalInfo;
        this.passwordInfo = passwordInfo;
        logs.add(this);
    }

    public String getAction() { return action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAdditionalInfo() { return additionalInfo; }
    public String getPasswordInfo() { return passwordInfo; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp).append(": ").append(action);
        if (additionalInfo != null) {
            sb.append(" - ").append(additionalInfo);
        }
        return sb.toString();
    }

    public static void logPasswordView(String userId, String hashedPassword, boolean isAdmin) {
        String userType = isAdmin ? "Admin" : "Farmer";
        String action = userType + " password viewed for ID: " + userId;
        String passwordInfo = String.format("Hashed Password (SHA-256): %s", hashedPassword);
        new AuditLog(action, "Password view logged", passwordInfo);
    }

    public static List<AuditLog> getLogs() {
        return logs;
    }
}
