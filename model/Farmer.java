package com.model;

import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Farmer {
    private String id;
    private String name;
    private String farmLocation;
    private String passwordHash;
    private List<Cattle> cattle;

    public Farmer(String id, String name, String farmLocation, String password) {
        this.id = id;
        this.name = name;
        this.farmLocation = farmLocation;
        this.passwordHash = hashPassword(password);
        this.cattle = new ArrayList<>();
    }

   
    public String getId() { return id; }
    public String getName() { return name; }
    public String getFarmLocation() { return farmLocation; }
    public List<Cattle> getCattle() { return cattle; }

    
    public void setName(String name) { this.name = name; }
    public void setFarmLocation(String farmLocation) { this.farmLocation = farmLocation; }

    
    public boolean checkPassword(String inputPassword) {
        return this.passwordHash.equals(hashPassword(inputPassword));
    }

    public void setPassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
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
        AuditLog.logPasswordView(this.id, this.passwordHash, false);
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
