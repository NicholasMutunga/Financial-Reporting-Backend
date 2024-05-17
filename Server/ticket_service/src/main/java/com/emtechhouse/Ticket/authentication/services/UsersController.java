package com.emtechhouse.Ticket.authentication.services;

import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.Utils.Security.jwt.CurrentUserContext;
import com.emtechhouse.Ticket.Utils.Security.jwt.JwtUtils;
import com.emtechhouse.Ticket.authentication.Role.ERole;
import com.emtechhouse.Ticket.authentication.Role.Role;
import com.emtechhouse.Ticket.authentication.models.*;
import com.emtechhouse.Ticket.authentication.Role.RoleRepository;
import com.emtechhouse.Ticket.authentication.repositories.TelleraccountRepo;
import com.emtechhouse.Ticket.authentication.repositories.UsersRepository;
import com.emtechhouse.Ticket.authentication.workclass.WorkclassRepo;
import com.emtechhouse.Ticket.authentication.utilities.PasswordGeneratorUtil;
import com.emtechhouse.Ticket.authentication.utilities.ServiceCaller;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import lombok.extern.slf4j.Slf4j;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("system/ticketing/auth")
@Slf4j
public class UsersController {

    @Value("${spring.application.useOTP}")
    private String useOTP;

    @Value("${spring.application.otpTestMail}")
    private String otpTestMail;

    @Value("${spring.application.otpProd}")
    private String otpProd;

    @Value("${organisation.maxNoOfTellers}")
    private Integer maxNoOfTellers;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    AuthSessionService authSessionService;

    @Autowired
    OTPService otpService;

    @Autowired
    SMSService smsService;

    @Autowired
    ServiceCaller serviceCaller;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private WorkclassRepo workclassRepo;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TelleraccountRepo telleraccountRepo;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/signin/new")
    public EntityResponse signin(@Validated @RequestBody LoginRequest loginRequest, HttpServletRequest request) throws MessagingException {
        log.info("Arrived to login");
        System.out.println("Username: "+loginRequest.getUsername() + " Password: "+loginRequest.getPassword());
        EntityResponse response = new EntityResponse();
        Users user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        log.info("User: "+user.getUsername());
        if (user == null) {
            response.setMessage("Account Access RESTRICTED!!! Check with the System Admin");
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return response;
        } else {
//        Check if Account is Locked
            if (user.isAcctLocked) {
                response.setMessage("Account is Locked!");
                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                return response;
            } else {
                if (user.getDeletedFlag() == 'Y') {
                    response.setMessage("This account has been deleted!");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                    return response;
                } else {
//                    Authentication authentication = authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("After authentication");
//            If he is a teller, he must have an account mapping
                    if (user.getIsTeller().equalsIgnoreCase("Yes")) {
                        log.info("teller account");
                        Optional<Telleraccount> telleraccount = telleraccountRepo.findByEntityIdAndTellerIdAndDeletedFlag(user.getEntityId(), String.valueOf(user.getSn()), 'N');
//                        if (telleraccount.isPresent()) {
//                            if (useOTP.equalsIgnoreCase("false")) {
//                                log.info("login.................");
//                                JwtResponse jwtResponse = getAccessToken(loginRequest.getUsername());
//                                jwtResponse.setTellerAc(telleraccount.get().getTellerAc());
//                                jwtResponse.setAgencyAc(telleraccount.get().getAgencyAc());
//                                jwtResponse.setOtpEnabled(false);
//                                user.setIsAcctActive(true);
//                                userRepository.save(user);
//                                response.setMessage("Welcome " + user.getUsername() + ", You have been Authenticated Successfully at " + new Date());
//                                response.setStatusCode(HttpStatus.OK.value());
//                                response.setEntity(jwtResponse);
//                            } else {
//                                String email = otpTestMail;
//                                String otp = otpService.generateOTP(loginRequest.getUsername());
//                                String otpMessage = "Your otp code is " + otp;
//                                System.out.println(otpMessage);
//                                smsService.sendSMS(otpMessage, user.getPhoneNo());
////                                String otp = otpService.generateOTP(loginRequest.getUsername());
//                                if (otpProd.equalsIgnoreCase("true")){
//                                    email = user.getEmail();
//
//                                }else {
//                                    email = email;
//                                }
//                                String to = email;
//                                String subject = "OTP Verification";
//                                String message = "Your OTP is "+otp;
//
////                                mailService.sendEmail(to, message, subject);
//
//                                serviceCaller.sendEmail(new MailDto(message,email,subject,user.getPhoneNo()));
//
//                                response.setMessage("Welcome " + user.getUsername() + ", Application 2FA Has Started and Verification OTP has been sent to your phone or email. Please VERIFY the Provided OTP to complete Authentication Process.");
//                                response.setStatusCode(HttpStatus.OK.value());
//                                JwtResponse jwtResponse = new JwtResponse();
//                                jwtResponse.setOtpEnabled(true);
//                                jwtResponse.setUsername(loginRequest.getUsername());
//                                response.setEntity(jwtResponse);
//                            }
//                        } else {
//                            response.setMessage("Sorry " + user.getUsername() + " Account Access RESTRICTED!! You MUST Have a Teller Account: !!");
//                            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
//                            response.setEntity("");
//                        }
                    }else{
                        log.info("not a teller account");
                        if (useOTP.equalsIgnoreCase("false")){
                            log.info("otp is false");
                            JwtResponse jwtResponse = getAccessToken(loginRequest.getUsername());
                            jwtResponse.setOtpEnabled(false);
                            user.setIsAcctActive(true);
                            userRepository.save(user);
                            response.setMessage("Welcome " + user.getUsername() + ", You have been Authenticated Successfully at " + new Date());
                            response.setStatusCode(HttpStatus.OK.value());
                            response.setEntity(jwtResponse);
                            String activity = "Sign in";
                            Character isActive = 'Y';
                            authSessionService.saveLoginSession(request, user.getUsername(), activity, isActive);
                        }else {
                            log.info("otp is true");
                            String email = otpTestMail;
                            String otp = otpService.generateOTP(loginRequest.getUsername());
                            String otpMessage = "Your otp code is " + otp;
                            System.out.println(otpMessage);
                            smsService.sendSMS(otpMessage, user.getPhoneNo());
//                            String otp = otpService.generateOTP(loginRequest.getUsername());
                            if (otpProd.equalsIgnoreCase("true")){
                                email = user.getEmail();
                            }else {
                                email = email;
                            }
                            String to = email;
                            String subject = "OTP Verification";
                            String message = "Your OTP is "+otp;
//                            mailService.sendEmail(to, message, subject);
                            serviceCaller.sendEmail(new MailDto(message,email,subject,user.getPhoneNo()));
                            response.setMessage("Welcome " + user.getUsername() + ", Application 2FA Has Started and Verification OTP has been sent to your phone or email. Please VERIFY the Provided OTP to complete Authentication Process.");
                            response.setStatusCode(HttpStatus.OK.value());
                            JwtResponse jwtResponse = new JwtResponse();
                            jwtResponse.setOtpEnabled(true);
                            jwtResponse.setUsername(loginRequest.getUsername());
                            response.setEntity(jwtResponse);
                        }
                    }
                }
            }
        }
        return response;
    }


    private JwtResponse getAccessToken(String username){
        Optional<Users> userCheck = userRepository.findByUsername(username);
        log.info("User check: "+userCheck.isPresent());
        Users user = userCheck.get();
        String jwt = jwtUtils.generateJwtTokenWithUsername(username);
        Workclass workclass = null;
        JwtResponse jwtResponse = new JwtResponse();
        // get workclass from user
        Optional<Workclass> workclass1 = workclassRepo.findById(user.getWorkclassFk());
        Optional<Telleraccount> telleraccount = telleraccountRepo.findByEntityIdAndDeletedFlagAndTellerUserName(EntityRequestContext.getCurrentEntityId(), 'N', username);
        if (workclass1.isPresent()){
            workclass = workclass1.get();

        }
        if(telleraccount.isPresent()){
            jwtResponse.setTellerAc(telleraccount.get().getTellerAc());
            jwtResponse.setAgencyAc(telleraccount.get().getAgencyAc());
        }
        Set<Role> roles = user.getRoles();

        jwtResponse.setToken(jwt);
        jwtResponse.setId(user.getSn().longValue());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setSolCode(user.getSolCode());
        jwtResponse.setEntityId(user.getEntityId());
        jwtResponse.setFirstName(user.getFirstName());
        jwtResponse.setLastName(user.getLastName());
        jwtResponse.setFirstLogin(user.getFirstLogin());
        jwtResponse.setMemberCode(user.getMemberCode());
        jwtResponse.setIsSystemGenPassword(user.getIsSystemGenPassword());
        jwtResponse.setRoles(roles);
        jwtResponse.setWorkclasses(workclass);
        log.info("User check1: "+userCheck.isPresent());
        jwtResponse.setUuid(CurrentUserContext.getCurrentActiveUser().getUuid());
        jwtResponse.setStatus(CurrentUserContext.getCurrentActiveUser().getStatus());
        jwtResponse.setLoginAt(CurrentUserContext.getCurrentActiveUser().getLoginAt());
        jwtResponse.setAddress(CurrentUserContext.getCurrentActiveUser().getAddress());
        jwtResponse.setOs(CurrentUserContext.getCurrentActiveUser().getOs());

        jwtResponse.setBrowser(CurrentUserContext.getCurrentActiveUser().getBrowser());
        return jwtResponse;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) throws MessagingException {
        EntityResponse response = new EntityResponse();
        if (UserRequestContext.getCurrentUser().isEmpty()) {
            response.setMessage("User Name not present in the Request Header");
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
            response.setEntity("");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                response.setMessage("Entity not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                PasswordGeneratorUtil passwordGeneratorUtil = new PasswordGeneratorUtil();
                String generatedPassword = passwordGeneratorUtil.generatePassayPassword();
                // Create new user's account
                signUpRequest.setPassword(generatedPassword);
                if (validateUser(signUpRequest).getStatusCode() == HttpStatus.OK.value()){
                    Users user = new Users();
                    Set<Role> roles = new HashSet<>();
                    Optional<Role> role = roleRepository.findById(Long.valueOf(signUpRequest.getRoleFk()));
                    if (role.isPresent()) {
                        roles.add(role.get());
                    } else {
                        Optional<Role> defRole = roleRepository.findByName(ERole.ROLE_USER.toString());
                        roles.add(defRole.get());
                    }
                    user.setRoles(roles);
                    user.setWorkclassFk(Long.valueOf(signUpRequest.getWorkclassFk()));
                    user.setPostedTime(new Date());
                    user.setPostedFlag('Y');
                    user.setPostedBy(UserRequestContext.getCurrentUser());
                    user.setIsAcctLocked(false);
                    user.setFirstLogin('Y');
                    user.setEntityId(signUpRequest.getEntityId());
                    user.setFirstName(signUpRequest.getFirstName());
                    user.setLastName(signUpRequest.getLastName());
                    user.setPhoneNo(signUpRequest.getPhoneNo());
                    user.setSolCode(signUpRequest.getSolCode());
                    user.setEmail(signUpRequest.getEmail());
                    user.setUsername(signUpRequest.getUsername());
                    user.setEntityId(signUpRequest.getEntityId());
                    user.setMemberCode(signUpRequest.getMemberCode());
                    user.setPassword(encoder.encode(signUpRequest.getPassword()));
//                check if the teller list exceeded
                    if (signUpRequest.getIsTeller().equalsIgnoreCase("Yes") && userRepository.findByIsTellerAndDeletedFlag("Yes", 'N').size() >= maxNoOfTellers) {
                        response.setMessage("Max No. of Tellers Exceeded! " + maxNoOfTellers);
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        user.setIsTeller(signUpRequest.getIsTeller());

                        userRepository.save(user);
                        String mailMessage = "Dear " + user.getFirstName() + " your account has been successfully created using username " + user.getUsername()
                                + " and password " + signUpRequest.getPassword() + " Login in to change your password.";
//                        mailService.sendEmail(user.getEmail(), mailMessage, "Account Successfully Created");
                        Mailparams mailsample = new Mailparams();
                        mailsample.setEmail(user.getEmail());
                        mailsample.setSubject("Account Successfully Created");
                        mailsample.setMessage(mailMessage);
                        response.setMessage("User " + user.getUsername() + " has been registered successfully! The password has been sent to the registered email address " + user.getEmail());
                        System.out.println("USER CREATED SUCCESSFULLY");
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }else{
                    return new ResponseEntity<>(validateUser(signUpRequest), HttpStatus.OK);
                }
            }
        }
    }


    public EntityResponse validateUser(SignupRequest signupRequest){
        EntityResponse response = new EntityResponse();
        SourceRule sourceRule = new SourceRule();
        HistoryRule historyRule = new HistoryRule();
        PasswordData passwords = new PasswordData(signupRequest.getPassword());
//        TODO: Password Should not contain username
        Rule rule = new UsernameRule();
        PasswordValidator usernamevalidator = new PasswordValidator(rule);
        passwords.setUsername(signupRequest.getUsername());
        RuleResult results = usernamevalidator.validate(passwords);
        if(results.isValid()){
//            TODO: Username is unique
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                response.setMessage("Username is already taken! "+signupRequest.getUsername());
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }else {
//                TODO: Email is unique
                if (userRepository.existsByEmail(signupRequest.getEmail())) {
                    response.setMessage("Email is already in use!");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    return response;
                }else{
//                    TODO: Phone number is unique
                    if (userRepository.existsByPhoneNo(signupRequest.getPhoneNo())) {
                        response.setMessage("The Phone number is already registered to another account!");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        return response;
                    }else {
//                        TODO: Check if user has a Role
                        if (signupRequest.getRoleFk() == null) {
                            response.setMessage("You must provide a role!");
                            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                            response.setEntity("");
                            return response;
                        } else {
//                            TODO: Check if user has a workclass
                            if (signupRequest.getWorkclassFk() == null) {
                                response.setMessage("You must provide a workclass!");
                                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                                response.setEntity("");
                                return response;
                            }else {
                                response.setMessage("User is valid");
                                response.setStatusCode(HttpStatus.OK.value());
                            }
                        }
                    }

                }
            }

        }else{
            response.setMessage("Password should not contain the username provided i.e "+signupRequest.getUsername());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @GetMapping(path = "/users")
    public List<Users> allUsers() {
        List<Users> users = userRepository.findAllByDeletedFlag('N');
        return userRepository.findAllByDeletedFlag('N');
    }
    @GetMapping(path = "/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }
}
