package com.cms.model;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Cattle {
    private String uid;
    private String breed;
    private int age;
    private double weight;
    private List<String> vaccinationRecords;
    private InsurancePolicy insurancePolicy;
    private String checksum;
    private boolean isVaccinated;
    private String vaccinationDate;
    
    public Cattle(String uid, String breed, int age, double weight, boolean isVaccinated, String vaccinationDate) {
        this.uid = uid;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.isVaccinated = isVaccinated;
        this.vaccinationDate = vaccinationDate;
        this.vaccinationRecords = new ArrayList<>();
        updateChecksum();
    }
    
    public boolean isVaccinated() { return isVaccinated; }
    public String getVaccinationDate() { return vaccinationDate; }
    
    public void setVaccinationStatus(boolean vaccinated, String date) {
        this.isVaccinated = vaccinated;
        this.vaccinationDate = date;
        updateChecksum();
    }
    
    @Override
    public void updateChecksum() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = uid + breed + age + weight + isVaccinated + vaccinationDate;
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

    public String getUid() { return uid; }
    public int getAge() { return age; }
    public void setInsurancePolicy(InsurancePolicy policy) { this.insurancePolicy = policy; }

    public String getBreed() { return breed; }
    public double getWeight() { return weight; }
    
    public void setBreed(String breed) {
        this.breed = breed;
        updateChecksum();
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
        updateChecksum();
    }
}