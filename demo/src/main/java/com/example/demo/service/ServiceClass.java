package com.example.demo.service;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.model.ForgotPassword;
import com.example.demo.model.Form;
import com.example.demo.model.Login;
import com.example.demo.rep.LoginRepository;
import com.example.demo.rep.UserRepository;
import com.example.response.ResponseBody;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v118.network.Network;
import org.openqa.selenium.devtools.v118.network.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServiceClass {
    WebDriver driver;
    DevTools devTools;

    ResponseBody responseBody;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    public ResponseBody getResponse() {

        responseBody = new ResponseBody();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response resp = responseReceived.getResponse();
            responseBody.setStatusCode(resp.getStatus());
            responseBody.setUrl(resp.getUrl());
            responseBody.setHeader(String.valueOf(resp.getRequestHeaders()));


        });

        return responseBody;

    }

    public ResponseBody returnAPIsforPage(String page) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        devTools = ((ChromeDriver) driver).getDevTools();
        driver.get("https://recruiter.bigshyft.com/");
        responseBody = getResponse();
        return responseBody;
    }

    public String addDetailsToDB(Form details) {
        String detailsValid = verifyValidations(details);

        if (detailsValid.equals("ok")) {
            details.setPassword(passwordEncoder.encode(details.getPassword()));
            Form save = this.userRepository.insert(details);
            ResponseEntity.ok(save);

        }

        return detailsValid;
    }

    public String uploadResume(MultipartFile resume, String id) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", resume.getSize());
        Object fileId = gridFsTemplate.store(resume.getInputStream(), resume.getOriginalFilename(), resume.getContentType(), metadata);
        System.out.println(addResumeIdToDocument(id, fileId.toString()));
        return fileId.toString();
    }

    public String verifyValidations(Form details) {
        boolean emailValid = verifyEmail(details.getEmail());
        boolean nameValid = verifyName(details.getName());
        if (!emailValid) {
            return "Invalid email!";

        }

        if (!nameValid) {
            return "Invalid name!";
        }

        return "ok";

    }

    public String addResumeIdToDocument(String id, String resumeId) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoCollection<Document> collection = mongoClient.getDatabase("myForm")
                    .getCollection("user");
            Bson filter = Filters.eq("_id", new ObjectId(id));
            Bson updateOperation = Updates.set("resumeId", resumeId); // Replace fieldName and fieldValue with your field and value
            collection.updateOne(filter, updateOperation);

            return "ok";
        }
    }

    public String deleteEntryFromDB(String id) {

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoCollection<Document> collection = mongoClient.getDatabase("myForm")
                    .getCollection("user");
            System.out.println("extracted " + jwtAuthFilter.giveExtractedUsername());
            Bson filter = Filters.eq("email", jwtAuthFilter.giveExtractedUsername());
            Document user = collection.find(filter).first();
            assert user != null;
            String profileId = user.getString("_id");
            System.out.println(profileId + " " + id);
            collection.deleteOne(new Document("_id", new ObjectId(id)));
            return "ok";
        } catch (Exception e) {
            return "error";


        }
    }


    public boolean verifyEmail(String email) {
        String regex = "^(.+)@(\\S+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean verifyName(String name) {
        String regex = "\\D*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public Boolean verifyPasswordAndLoginUser(Form loginDetails) {

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoCollection<Document> collection = mongoClient.getDatabase("myForm")
                    .getCollection("user");
            Bson filter = Filters.eq("email", loginDetails.getEmail());
            Document user = collection.find(filter).first();
            if (user == null)
                return false;
            String password = user.getString("password");
            System.out.println(password);
            return password.equals(loginDetails.getPassword());
            //need to reset attempts here too
        }
    }

    public Boolean storeLoginFailureAttempt(String email) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoCollection<Document> collection = mongoClient.getDatabase("myForm")
                    .getCollection("loginAttempts");
            Bson filter = Filters.eq("email", email);
            Document existingUser = collection.find(filter).first();
            if (existingUser == null) {
                Login login = new Login();
                login.setEmail(email);
                login.setAttempts(1);
                login.setTime(System.currentTimeMillis());
                Login save = this.loginRepository.insert(login);
                ResponseEntity.ok(save);
                return false;
            } else {
                Integer attempts = existingUser.getInteger("attempts");
                attempts = attempts + 1;
                Bson updateOperation = Updates.set("attempts", attempts);
                Bson updateTime = Updates.set("time", System.currentTimeMillis());
                collection.updateOne(filter, updateOperation);
                collection.updateOne(filter, updateTime);
                return (checkLoginAttemptsExceeded(email));
            }
        }
    }

    public Boolean checkLoginAttemptsExceeded(String email) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoCollection<Document> collection = mongoClient.getDatabase("myForm").getCollection("loginAttempts");
            Bson filter = Filters.eq("email", email);
            Document user = collection.find(filter).first();
            assert user != null;
            Integer attempts = user.getInteger("attempts");
            Long time = user.getLong("time");
            if (attempts > 3) {
                if (System.currentTimeMillis() - time > 30000) {
                    Bson updateOperation = Updates.set("attempts", 0);
                    collection.updateOne(filter, updateOperation);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void loginFailedFreeze(String email) {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> loginFailedWaitMessage() {
        return ResponseEntity.ok("Retry after 30 seconds.....");
    }

    public Boolean changePassword(ForgotPassword forgotPassword) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoCollection<Document> collection = mongoClient.getDatabase("myForm").getCollection("user");
            Bson filter = Filters.eq("email", forgotPassword.getEmail());
            Document user = collection.find(filter).first();
            if (user == null)
                return false;
            Bson updateOperation = Updates.set("password", forgotPassword.getPassword());
            collection.updateOne(filter, updateOperation);

        } catch (Exception ignored) {

        }
        return  true;
    }

}
