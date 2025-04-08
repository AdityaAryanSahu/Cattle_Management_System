
package com.javafx;

import com.cms.model.InsurancePolicy;
import com.cms.model.*;
import com.cms.model.Farmer;
import com.cms.model.Cattle;
import com.cms.model.InsuranceClaim;
import com.cms.service.CMSService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class trial extends Application {
    private Stage stage;
    private Scene scene1;
    private Scene scene2;
    private Scene scene3;
    private CMSService cmsService=new CMSService();
    private Cattle cattle;
    private Farmer farmer;
    private List<Cattle> cattleList;
    private List<AuditLog> auditLogs;
    private List<InsuranceClaim> insuranceClaims;
    private Admin admin;
    private InsurancePolicy policy;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Cattle Management System");

        scene1 = createSceneOne();
        scene2 = createSceneTwo();
        scene3 = createSceneThree();


        stage.setScene(scene1);

        stage.show();
    }


    /////scene 1 main page
    private Scene createSceneOne() {

        TabPane tabPane = new TabPane();

        Tab farmerTab = new Tab("Farmer Registration");
        farmerTab.setContent(createFarmerRegistrationPane());
        farmerTab.setStyle("-fx-background-color: #fff5ee;");
        farmerTab.setClosable(false);

        Tab farmer2Tab = new Tab("Farmer Login");
        farmer2Tab.setContent(createFarmerLoginPane());
        farmer2Tab.setStyle("-fx-background-color: #fff5ee;");
        farmer2Tab.setClosable(false);

        Tab adminTab = new Tab("Admin Registration");
        adminTab.setContent(createAdminRegistrationPane());
        adminTab.setStyle("-fx-background-color: #fff5ee;");
        adminTab.setClosable(false);

        Tab admin2Tab = new Tab("Admin Login");
        admin2Tab.setContent(createAdminLoginPane());
        admin2Tab.setStyle("-fx-background-color: #fff5ee;");
        admin2Tab.setClosable(false);

        tabPane.getTabs().addAll(farmerTab,farmer2Tab, adminTab,admin2Tab);

        Scene scene = new Scene(tabPane, 700, 600);
        return scene;
    }

    ////scene 2 cattle part
    private Scene createSceneTwo() {

        TabPane tabPane = new TabPane();

        Tab cattleTab = new Tab("Cattle Registration");
        cattleTab.setContent(createCattleRegistrationPane());
        cattleTab.setStyle("-fx-background-color: #fff5ee;");
        cattleTab.setClosable(false);

        Tab viewCattle=new Tab("View cattle details");
        viewCattle.setContent(createCattleDetails());
        viewCattle.setStyle("-fx-background-color: #fff5ee;");
        viewCattle.setClosable(false);

        Tab InsauranceClaimTab = new Tab("Insurance Claim");
        InsauranceClaimTab.setContent(createInsuranceClaimPane());
        InsauranceClaimTab.setStyle("-fx-background-color: #fff5ee;");
        InsauranceClaimTab.setClosable(false);

        Tab InsaurancePolicyTab = new Tab("Insurance Policy and premium");
        InsaurancePolicyTab.setContent(createInsurancePolicyPane());
        InsaurancePolicyTab.setStyle("-fx-background-color: #fff5ee;");
        InsaurancePolicyTab.setClosable(false);

        tabPane.getTabs().addAll(cattleTab,viewCattle,InsaurancePolicyTab,InsauranceClaimTab);

        Scene scene = new Scene(tabPane, 700, 600);
        return scene;

    }

    ////scene 3 admin page
    private Scene createSceneThree() {

        TabPane tabPane = new TabPane();

        Tab auditlog=new Tab("View Audit logs");
        auditlog.setContent(createViewAuditLogs());
        auditlog.setStyle("-fx-background-color: #fff5ee;");
    auditlog.setClosable(false);

        Tab manageclaims=new Tab("Manage Insaurance claims");
        manageclaims.setContent(createManageClaims());
        manageclaims.setClosable(false);

        manageclaims.setStyle("-fx-background-color: #fff5ee;");

        tabPane.getTabs().addAll(auditlog,manageclaims);
        Scene scene = new Scene(tabPane, 700, 600);
        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }
    private void switchScenes(Scene scene) {
        stage.setScene(scene);
    }


    ////farmer registration page
    private VBox createFarmerRegistrationPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField farmerIdField = new TextField();
        farmerIdField.setPromptText("Farmer ID");
        TextField farmerNameField = new TextField();
        farmerNameField.setPromptText("Farmer Name");
        TextField farmLocationField = new TextField();
        farmLocationField.setPromptText("Farm Location");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label messageLabel=new Label();
        Button registerButton = new Button("Register Farmer");
        registerButton.setOnAction(e -> {
            String id = farmerIdField.getText();
            String name = farmerNameField.getText();
            String location = farmLocationField.getText();
            String password = passwordField.getText();
            boolean success = cmsService.registerFarmer(id, name, location, password);
            messageLabel.setText(success? "Registration successful": "ID already exits");
            farmerIdField.clear();
            farmerNameField.clear();
            farmLocationField.clear();
            passwordField.clear();
        });
        Button cl=new Button("Clear");
        cl.setOnAction(e->{
            messageLabel.setText(""); farmerIdField.clear();
            passwordField.clear();farmerNameField.clear();
            farmLocationField.clear();
        });
        Button exit=new Button("Exit");
        exit.setOnAction(e->{
            Platform.exit();
        });
        Label my=new Label("Press Clear after registration");
        vbox.getChildren().addAll(farmerIdField, farmerNameField, farmLocationField, passwordField, registerButton,
                messageLabel,my,cl,exit);
        return vbox;
    }

    ////farmer login page
    private VBox createFarmerLoginPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField farmerIdField = new TextField();
        farmerIdField.setPromptText("Farmer ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label messageLabel=new Label();
        Button registerButton = new Button("Farmer Login");
        registerButton.setOnAction(e -> {
            String id = farmerIdField.getText();
            String password = passwordField.getText();
            Farmer farmer=cmsService.login(id,password);

            if(farmer==null) {
                messageLabel.setText("Cant login: Wrong Credentials");
            }
            else {messageLabel.setText("Farmer logged in: "+id); this.farmer = farmer;
                cattleList=farmer.getCattle();
                switchScenes(scene2);
            }
            farmerIdField.clear();
            passwordField.clear();

        });
        Button cl=new Button("Clear");
        cl.setOnAction(e->{
            messageLabel.setText(""); farmerIdField.clear();
            passwordField.clear();
        });
        Button exit=new Button("Exit");
        exit.setOnAction(e->{
            Platform.exit();
        });
        Label my=new Label("Press Clear after login");
        vbox.getChildren().addAll(farmerIdField, passwordField, registerButton,messageLabel,my,cl,exit);
        return vbox;
    }

    ///admin registration page
    private VBox createAdminRegistrationPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField adminIdField = new TextField();
        adminIdField.setPromptText("Admin ID");
        TextField adminNameField = new TextField();
        adminNameField.setPromptText("Admin Name");
        TextField departmentField = new TextField();
        departmentField.setPromptText("Department");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label messageLabel=new Label();
        Button registerButton = new Button("Register Admin");
        registerButton.setOnAction(e -> {
            String id = adminIdField.getText();
            String name = adminNameField.getText();
            String department = departmentField.getText();
            String password = passwordField.getText();
            boolean success = cmsService.registerAdmin(id, name, department, password);
            messageLabel.setText(success? "Registration successful": "ID already exits");
            adminIdField.clear();
            passwordField.clear();
            departmentField.clear();
            adminNameField.clear();
        });
        Button exit=new Button("Exit");
        exit.setOnAction(e->{
            Platform.exit();
        });
        Button cl=new Button("Clear");
        cl.setOnAction(e->{
            messageLabel.setText(""); adminIdField.clear();
            passwordField.clear();
        });

        vbox.getChildren().addAll(adminIdField, adminNameField, departmentField, passwordField, registerButton,messageLabel,cl,exit);
        return vbox;
    }

    ///admin login page
    private VBox createAdminLoginPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField adminIdField = new TextField();
        adminIdField.setPromptText("Admin ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label messageLabel=new Label();
        Button registerButton = new Button("Login Admin");
        registerButton.setOnAction(e -> {
            String id = adminIdField.getText();
            String password = passwordField.getText();
            Admin admin=cmsService.loginAdmin(id,password);
            if(admin==null)
                messageLabel.setText("Cant login: Wrong Credentials");
            else {
                messageLabel.setText("Admin logged in: "+id);
                this.admin=admin;
                switchScenes(scene3);
            }
            adminIdField.clear();
            passwordField.clear();
        });
        Button exit=new Button("Exit");
        exit.setOnAction(e->{
            Platform.exit();
        });
        Button cl=new Button("Clear");
        cl.setOnAction(e->{
            messageLabel.setText(""); adminIdField.clear();
            passwordField.clear();
        });

        vbox.getChildren().addAll(adminIdField, passwordField, registerButton,messageLabel,cl,exit);
        return vbox;
    }

    ///view audit logs
    private VBox createViewAuditLogs(){
        VBox vbox=new VBox(5);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        auditLogs=AuditLog.getLogs();
        Label mylabel=new Label("Click 'Show audit logs ' to view logs");
        Button b2=new Button("Logout");
        b2.setOnAction(e1->switchScenes(scene1));
        Button b3=new Button("Clear");
        Button b=new Button("Show audit logs");
        b.setOnAction(e->{
            if (auditLogs == null || auditLogs.isEmpty()) {
                mylabel.setText("No log registered.");
                return;
            }
            Label text;
            Label text2;

            for (AuditLog audit : auditLogs) {
                text=new Label();
                text2=new Label();
                System.out.println(audit.toString());
                text.setText("log: " + audit.toString());
                if(audit.getPasswordInfo()!=null){
                    text2.setText(audit.getPasswordInfo()+"\n");
                    System.out.println(audit.getPasswordInfo());}
                vbox.getChildren().addAll(text,text2);
            }

        });
        b3.setOnAction(e1->{
            vbox.getChildren().clear();
            mylabel.setText("Click 'Show audit list' to view logs");
            Label la=new Label("Clear before logout");
            vbox.getChildren().addAll(mylabel,la,b,b2,b3);
        });
        vbox.getChildren().add(b3);
        vbox.getChildren().addAll(b);
        return vbox;
    }

    ///cattle details
    private VBox createCattleDetails(){
        if (this.farmer != null) {
            cattleList = this.farmer.getCattle();
            if (cattleList == null) {
                cattleList = new ArrayList<>();
            }
        }
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        Label messageLabel = new Label("Press 'Show details' to view cattle information");
        Label my=new Label("Clear before registering more cattle or changing tabs");
        Button showDetailsButton = new Button("Show details");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e2 -> switchScenes(scene1));
        Button clearButton = new Button("Clear");
        vbox.getChildren().addAll(messageLabel,my);
        showDetailsButton.setOnAction(e -> {
            Label cattleDetails=new Label();
            if (cattleList == null || cattleList.isEmpty()) {
                cattleDetails.setText("No cattle registered.");
                vbox.getChildren().add(cattleDetails);
            }

            for (Cattle cattle : cattleList) {
                ObservableList<String> o= FXCollections.observableArrayList("Cattle ID: " + cattle.getUid(),
                        "Breed: " + cattle.getBreed() ,
                        "Age: " + cattle.getAge() ,
                        "Weight: " + cattle.getWeight() + " kg",
                        "Vaccination Status: "+cattle.isVaccinated(),
                        "Vaccination Date: "+cattle.getVaccinationDate()+"\n"
                        );
                ListView<String> list=new ListView<>(o);
                list.setPrefSize(100,150);

                vbox.getChildren().add(list);
            }
            clearButton.setOnAction(e1 -> {
                vbox.getChildren().clear();
                messageLabel.setText("Click 'show details' for new info");
                vbox.getChildren().addAll(messageLabel,my,showDetailsButton,logoutButton,clearButton);
            });


        });
        vbox.getChildren().addAll(showDetailsButton);
        vbox.getChildren().add(clearButton);

        return vbox;
    }

    //cattle registration page
    private VBox createCattleRegistrationPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField farmerIdField = new TextField();
        farmerIdField.setPromptText("Farmer ID");
        TextField breedField = new TextField();
        breedField.setPromptText("Breed");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField weightField = new TextField();
        weightField.setPromptText("Weight");
        Label vaccinationLabel = new Label("Vaccination Status:");
        CheckBox vaccinatedCheckBox = new CheckBox("Vaccinated");

        DatePicker vaccinationDatePicker = new DatePicker();
        vaccinationDatePicker.setPromptText("Vaccination Date");
        vaccinationDatePicker.setDisable(true);

        vaccinatedCheckBox.setOnAction(e -> {
            vaccinationDatePicker.setDisable(!vaccinatedCheckBox.isSelected());
        });

        Label mylabel=new Label();
        Button registerButton = new Button("Register Cattle");
        registerButton.setOnAction(e -> {
            String farmerId = farmerIdField.getText();
            String breed = breedField.getText();
            int age = Integer.parseInt(ageField.getText());
            double weight = Double.parseDouble(weightField.getText());
            boolean isVaccinated = vaccinatedCheckBox.isSelected();
            String vaccinationDate = isVaccinated ?
                    vaccinationDatePicker.getValue().toString() : null;

            Cattle cattle = cmsService.registerCattle(
                    farmerId, breed, age, weight, isVaccinated, vaccinationDate);
            if(cattle!=null) {this.cattle=cattle;mylabel.setText("Registration successful");}
            else mylabel.setText("Registration failed");
            farmerIdField.clear();
            breedField.clear();
            ageField.clear();
            weightField.clear();
            vaccinatedCheckBox.setSelected(false);
            vaccinationDatePicker.setValue(null);
            vaccinationDatePicker.setDisable(true);

        });
        Button b=new Button("Clear");
        b.setOnAction(e->{mylabel.setText("");
            farmerIdField.clear();
            breedField.clear();
            ageField.clear();
            weightField.clear();
            vaccinatedCheckBox.setSelected(false);
            vaccinationDatePicker.setValue(null);
            vaccinationDatePicker.setDisable(true);
        });

        Button b2=new Button("Logout");
        b2.setOnAction(e->switchScenes(scene1));
        Label instructionLabel = new Label("Press Clear after registration");
        Label my=new Label("Press Clear after login");
        vbox.getChildren().addAll(
                farmerIdField, breedField, ageField, weightField,
                vaccinationLabel, vaccinatedCheckBox, vaccinationDatePicker,
                registerButton, mylabel, instructionLabel, b,b2
        );
     return vbox;
    }

    //insurance claim page
    private VBox createInsuranceClaimPane() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField cattleUidField = new TextField();
        cattleUidField.setPromptText("Cattle UID");
        TextField reasonField = new TextField();
        reasonField.setPromptText("Other Reason");
        Label mm=new Label("Select Reason");
        ChoiceBox<String> ch=new ChoiceBox<>();
        ch.getItems().addAll("STOLEN", "ILLNESS", "ACCIDENT", "NATURAL_DISASTER","OTHER");
        ch.setValue("STOLEN");
        Label other=new Label("Enter Reason Only If 'OTHER' Selected Above");
        Label mylabel=new Label();
        Button fileClaimButton = new Button("File Insurance Claim");
        fileClaimButton.setOnAction(e -> {
            String cattleUid = cattleUidField.getText();
            String reason;
            if(ch.getValue().equals("OTHER"))
                reason = reasonField.getText();
            else reason=ch.getValue();
            InsuranceClaim claim = cmsService.fileClaim(cattleUid, reason);
            if(claim!=null) mylabel.setText("Insurance Claimed");
            else mylabel.setText("Sorry,Can't Claim Insurance");
        });
        Button b=new Button("Logout");
        b.setOnAction(e->switchScenes(scene1));

        Button b2=new Button("clear");
        b2.setOnAction(e-> {
            cattleUidField.clear();
            reasonField.clear();
            ch.setValue("STOLEN");
            mylabel.setText("");
        });
        Label my=new Label("Press Clear after claim");
        vbox.getChildren().addAll(cattleUidField, mm,ch,other,reasonField, fileClaimButton,mylabel,my,b2,b);
        return vbox;
    }

    ///insurance policy
    private VBox createInsurancePolicyPane(){
        VBox vbox=new VBox();
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        TextField cattleUidField = new TextField();
        cattleUidField.setPromptText("Cattle UID");
        Label mylabel=new Label("Enter Cattle UID");
        Button b=new Button("Show Policy");
        Button clear=new Button("Clear");
        Button b2=new Button("Logout");
        vbox.getChildren().add(mylabel);
        b2.setOnAction(e1->switchScenes(scene1));
        b.setOnAction(e->{
            String cattleUid = cattleUidField.getText();
            Double pre=cmsService.calculatePremium(this.cattle);
            InsurancePolicy policy=cmsService.createInsurancePolicy(cattleUid,pre);
            if(policy!=null){
                this.policy=policy;
                Label text=new Label("Policy ID: "+policy.policyId+"\nStartDate: "+policy.startDate+"\nEndDate: "+policy.endDate+
                        "\nStatus: "+policy.status+ "\nPremium: "+pre);
                vbox.getChildren().add(text);
            }

            clear.setOnAction(e2->{
                vbox.getChildren().clear();
                cattleUidField.clear();
                Label my=new Label("Click show to view policy\n");
                vbox.getChildren().setAll(cattleUidField,b,my,clear,b2);
            });

        });
        vbox.getChildren().addAll(cattleUidField,b,clear,b2);
        return vbox;
    }

    ///managing claims(Admin)
    private VBox createManageClaims(){
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #e6e6fa;");
        insuranceClaims = cmsService.getAllInsuranceClaims();
        if (insuranceClaims == null) {
            insuranceClaims = new ArrayList<>();
        }
        TextField claimIDField = new TextField();
        claimIDField.setPromptText("Claim ID");
        Button showButton = new Button("Show");
        Button clear=new Button("Clear");
        Label messageLabel = new Label("Enter Next To Select A New Status For The Claim");

        showButton.setOnAction(e -> {
            vbox.getChildren().clear();
            Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(e3 -> switchScenes(scene1));
            Text t = new Text();
            if (insuranceClaims.isEmpty()) {
                t.setText("No Pending Claims Available");
                vbox.getChildren().add(t);
            } else {
                for (InsuranceClaim claim : insuranceClaims) {
                    if (claim.getReason().equals(InsuranceClaim.ClaimReason.OTHER)) {
                        Text claimDetails = new Text("ClaimID: " + claim.getClaimId() +
                                "\nCattle UID: " + claim.getCattle().getUid() +
                                "\nReason: " + claim.getOtherReason() +
                                "\nStatus: " + claim.getStatus() + "\n");
                        vbox.getChildren().add(claimDetails);
                    } else {
                        Text claimDetails = new Text("ClaimID: " + claim.getClaimId() +
                                "\nCattle UID: " + claim.getCattle().getUid() +
                                "\nReason: " + claim.getReason() +
                                "\nStatus: " + claim.getStatus() + "\n");
                        vbox.getChildren().add(claimDetails);
                    }
                }
                vbox.getChildren().addAll(messageLabel, claimIDField);
            }
            vbox.getChildren().addAll(clear, logoutButton);

            Button nextButton = new Button("Next");
            nextButton.setOnAction(e4 -> {
                String claimID = claimIDField.getText();
                InsuranceClaim selectedClaim = insuranceClaims.stream()
                        .filter(claim -> claim.getClaimId().equals(claimID))
                        .findFirst()
                        .orElse(null);

                if (selectedClaim != null) {
                    Text statusText = new Text("Choose the status for Claim ID: " + claimID);

                    Button approvedButton = new Button("Approved");
                    approvedButton.setOnAction(e5 -> {
                        boolean success = cmsService.updateClaimStatus(claimID, InsuranceClaim.ClaimStatus.APPROVED);
                        if (success) {
                            statusText.setText("Claim approved.");
                        } else {
                            statusText.setText("Failed to approve claim.");
                        }
                    });

                    Button underReviewButton = new Button("Under Review");
                    underReviewButton.setOnAction(e5 -> {
                        boolean success = cmsService.updateClaimStatus(claimID, InsuranceClaim.ClaimStatus.UNDER_REVIEW);
                        if (success) {
                            statusText.setText("Claim is under review.");
                        } else {
                            statusText.setText("Failed to update claim status.");
                        }
                    });

                    Button rejectedButton = new Button("Rejected");
                    rejectedButton.setOnAction(e5 -> {
                        boolean success = cmsService.updateClaimStatus(claimID, InsuranceClaim.ClaimStatus.REJECTED);
                        if (success) {
                            statusText.setText("Claim rejected.");
                        } else {
                            statusText.setText("Failed to reject claim.");
                        }
                    });

                    clear.setOnAction(e6 -> {
                        claimIDField.clear();
                        vbox.getChildren().remove(t);
                        vbox.getChildren().removeAll(statusText, rejectedButton, approvedButton, underReviewButton);
                    });

                    vbox.getChildren().addAll(statusText, approvedButton, underReviewButton, rejectedButton);
                } else {
                    messageLabel.setText("No claim found with ID: " + claimID);
                }

            });

            vbox.getChildren().add(nextButton);

        });

        vbox.getChildren().addAll(showButton);
        return vbox;
    }

}


