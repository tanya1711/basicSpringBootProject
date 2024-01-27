package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.model.ForgotPassword;
import com.example.demo.model.Form;
import com.example.demo.model.Login;
import com.example.demo.model.response.ResponsePasswordChange;
import com.example.demo.rep.UserRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.KafkaMessagePublisher;
import com.example.demo.service.ServiceClass;
import com.github.dockerjava.api.model.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ControllerClass {
    @Autowired
    private ServiceClass formService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private KafkaMessagePublisher kafkaMessagePublisher;

    @Autowired
    private ResponsePasswordChange responsePasswordChange;


//    @Autowired
//    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/")
    public HttpStatus getTheForm() {
        return HttpStatus.OK;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/form")
    public String setFormValues(@RequestBody Form form) {
        System.out.println(form.getName());
        String isAdded = formService.addDetailsToDB(form);
        System.out.println(isAdded);
        return "resumeUpload";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/form/{id}")
    public void deleteUserEntry(@PathVariable String id) {
        System.out.println(id);
        String isDeleted = formService.deleteEntryFromDB(id);
        System.out.println(isDeleted);
    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/form/{name}")
//    public void findTheDetails(@PathVariable String name) {
//        System.out.println(userRepository.findByName(name).getEmail());
//    }
//
//    @PostMapping("/form/upload/{id}")
//    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @PathVariable String id) throws IOException {
//        return new ResponseEntity<>(formService.uploadResume(file, id), HttpStatus.OK);
//    }
//
////    @GetMapping("/login")
////    public String loginPage() {
////        return "login";
////    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "/login")
//    public void loginUser(@ModelAttribute Form loginDetails) throws Exception {
////        try {
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(loginDetails.getEmail(),loginDetails.getPassword())
////            );
////
////        }
////        catch (Exception e){
////            throw new Exception("invalid username/password");
////        }
//        System.out.println(jwtUtil.generateToken(loginDetails.getEmail()));
////        return jwtUtil.generateToken(loginDetails.getEmail());
//
//
//
//
////        if (formService.checkLoginAttemptsExceeded(loginDetails.getEmail())) {
////            return "loginAttemptExceeded";
////        } else {
////            Boolean loggedIn = formService.verifyPasswordAndLoginUser(loginDetails);
////            if (loggedIn) {
////                return "loginSuccess";
////            } else {
////                Boolean attemptExceeded = formService.storeLoginFailureAttempt(loginDetails.getEmail());
////                if (!attemptExceeded)
////                    return "loginFailure";
////                else
////                    return "loginAttemptExceeded";
////            }
////        }
//
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public Map<String,String> loginInAndGetToken(@RequestBody Login login) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        if (authenticate.isAuthenticated()) {
            Map<String, String> response = new HashMap<>();
            String token = jwtService.generateToken(login.getEmail());
            response.put("token",token);
            return response;

        } else
            return null;
        }

        @RequestMapping(method = RequestMethod.POST, value = "/produce/{message}")
       public  ResponseEntity<?> kafkaAddMessageAndCreateTopic(@PathVariable String message){
           kafkaMessagePublisher.sendMessageToTopic(message);
            return ResponseEntity.ok("send...");
        }

        @RequestMapping(method = RequestMethod.POST, value = "/forgotPassword")
        public ResponseEntity<?> sendEventToKakfa(@RequestBody ForgotPassword forgotPassword){
            ResponsePasswordChange.Message message = new ResponsePasswordChange.Message();
                kafkaMessagePublisher.sendEventsToTopic(forgotPassword);
                responsePasswordChange.setUserName(forgotPassword.getEmail());
                responsePasswordChange.setNewPassword(forgotPassword.getPassword());
                message.setMessage("true");
                message.setStatusCode(String.valueOf(HttpStatus.OK));
                responsePasswordChange.setResponseMessage(message);
                return ResponseEntity.ok(responsePasswordChange);

        }

//    @PostMapping("/logout")
//    public void logout(@RequestParam(value = "token", required = false) String tokenValue) {
//        if (tokenValue != null) {
//            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(tokenValue, null);
//            if (client != null) {
//                authorizedClientService.revokeAccessToken(client);
//            }
//        }
//        // Additional actions for complete logout (e.g., clearing session or cookies)
//    }


    }



