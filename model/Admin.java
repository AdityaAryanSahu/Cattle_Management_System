package com.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Admin {
    private String id;
    private String name;
    private String department;
    private String passwordHash;

    public Admin(String id, String name, String department, String password) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.passwordHash = hashPassword(password);
    }


    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }

  
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }

   
    public boolean checkPassword(String inputPassword) {
        return this.passwordHash.equals(hashPassword(inputPassword));
    }

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public String getHashedPassword() {
        AuditLog.logPasswordView(this.id, this.passwordHash, true);
        return this.passwordHash;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
