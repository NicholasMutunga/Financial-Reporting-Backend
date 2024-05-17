package com.emtechhouse.Ticket;


import com.emtechhouse.Ticket.authentication.Role.ERole;
import com.emtechhouse.Ticket.authentication.Role.Role;
import com.emtechhouse.Ticket.authentication.Role.RoleRepository;
import com.emtechhouse.Ticket.authentication.basicActions.Basicactions;
import com.emtechhouse.Ticket.authentication.models.*;
import com.emtechhouse.Ticket.authentication.repositories.*;
import com.emtechhouse.Ticket.authentication.basicActions.BasicactionsService;
import com.emtechhouse.Ticket.authentication.services.InitAuth;
import com.emtechhouse.Ticket.authentication.services.MailService;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import com.emtechhouse.Ticket.authentication.workclass.WorkclassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.util.*;

@SpringBootApplication
public class TicketApplication {

	@Autowired
	private UsersRepository userRepository;
	@Autowired
	EntityRepo entityRepo;
	@Autowired
	WorkclassRepo workclassRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	InitAuth initAuth;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	MailService mailService;


	@Autowired
	private PrivilegeRepo privilegeRepository;
	@Autowired
	private BasicactionsService basicactionsService;
	@Value("${organisation.superUserEmail}")
	private String superUserEmail;
	@Value("${organisation.superUserFirstName}")
	private String superUserFirstName;
	@Value("${organisation.superUserLastName}")
	private String superUserLastName;
	@Value("${organisation.superUserUserName}")
	private String superUserUserName;
	@Value("${organisation.superUserPhone}")
	private String superUserPhone;
	@Value("${organisation.superUserSolCode}")
	private String superUserSolCode;
	@Value("${organisation.superUserPassword}")
	private String superUserPassword;
	String NONE = "NONE";

	public static void main(String[] args) {
		SpringApplication.run(TicketApplication.class, args);
	}


	private void initEntity() {
		if (entityRepo.findAll().size() < 1) {
			Entitygroup entityGroup = new Entitygroup();
			entityGroup.setEntityCode("001");
			entityGroup.setEntityDescription("Main Entity");
			entityGroup.setEntityLocation("Kenya");
			entityGroup.setEntityEmail("sacco@gmail.com");
			entityGroup.setEntityPhoneNumber("");
			entityGroup.setPostedBy("System");
			entityGroup.setPostedFlag('Y');
			entityGroup.setVerifiedBy("System");
			entityGroup.setVerifiedFlag('Y');
			entityGroup.setVerifiedTime(new Date());
			entityGroup.setPostedTime(new Date());
			entityRepo.save(entityGroup);
		}
	}

	private void initRoles() {
		List<Role> currentRoles = roleRepository.findAll();
		List<Role> roleList = new ArrayList<>();
		if (roleRepository.findByName(ERole.ROLE_USER.toString()).isEmpty()) {
			Role userRole = new Role();
			userRole.setName(ERole.ROLE_USER.toString());
			userRole.setEntityId("001");
			userRole.setPostedBy("System");
			userRole.setPostedFlag('Y');
			userRole.setVerifiedBy("System");
			userRole.setVerifiedFlag('Y');
			userRole.setVerifiedTime(new Date());
			userRole.setPostedTime(new Date());
			roleList.add(userRole);
		}
		if (roleRepository.findByName(ERole.ROLE_TELLER.toString()).isEmpty()) {
			Role tellerRole = new Role();
			tellerRole.setEntityId("001");
			tellerRole.setPostedBy("System");
			tellerRole.setPostedFlag('Y');
			tellerRole.setVerifiedBy("System");
			tellerRole.setVerifiedFlag('Y');
			tellerRole.setVerifiedTime(new Date());
			tellerRole.setPostedTime(new Date());
			tellerRole.setName(ERole.ROLE_TELLER.toString());
			roleList.add(tellerRole);
		}
		if (roleRepository.findByName(ERole.ROLE_OFFICER.toString()).isEmpty()) {
			Role officerRole = new Role();
			officerRole.setEntityId("001");
			officerRole.setPostedBy("System");
			officerRole.setPostedFlag('Y');
			officerRole.setVerifiedBy("System");
			officerRole.setVerifiedFlag('Y');
			officerRole.setVerifiedTime(new Date());
			officerRole.setPostedTime(new Date());
			officerRole.setName(ERole.ROLE_OFFICER.toString());
			roleList.add(officerRole);
		}

		if (roleRepository.findByName(ERole.ROLE_SENIOR_OFFICER.toString()).isEmpty()) {
			Role seniorOfficerRole = new Role();
			seniorOfficerRole.setEntityId("001");
			seniorOfficerRole.setPostedBy("System");
			seniorOfficerRole.setPostedFlag('Y');
			seniorOfficerRole.setVerifiedBy("System");
			seniorOfficerRole.setVerifiedFlag('Y');
			seniorOfficerRole.setVerifiedTime(new Date());
			seniorOfficerRole.setPostedTime(new Date());
			seniorOfficerRole.setName(ERole.ROLE_SENIOR_OFFICER.toString());
			roleList.add(seniorOfficerRole);
		}
		if (roleRepository.findByName(ERole.ROLE_MANAGER.toString()).isEmpty()) {
			Role managerRole = new Role();
			managerRole.setEntityId("001");
			managerRole.setPostedBy("System");
			managerRole.setPostedFlag('Y');
			managerRole.setVerifiedBy("System");
			managerRole.setVerifiedFlag('Y');
			managerRole.setVerifiedTime(new Date());
			managerRole.setPostedTime(new Date());
			managerRole.setName(ERole.ROLE_MANAGER.toString());
			roleList.add(managerRole);
		}

		if (roleRepository.findByName(ERole.ROLE_NONE.toString()).isEmpty()) {
			Role noneRole = new Role();
			noneRole.setEntityId("001");
			noneRole.setPostedBy("System");
			noneRole.setPostedFlag('Y');
			noneRole.setVerifiedBy("System");
			noneRole.setVerifiedFlag('Y');
			noneRole.setVerifiedTime(new Date());
			noneRole.setPostedTime(new Date());
			noneRole.setName(ERole.ROLE_NONE.toString());
			roleList.add(noneRole);
		}

		if (roleRepository.findByName(ERole.ROLE_SUPERUSER.toString()).isEmpty()) {
			Role superuserRole = new Role();
			superuserRole.setEntityId("001");
			superuserRole.setPostedBy("System");
			superuserRole.setPostedFlag('Y');
			superuserRole.setVerifiedBy("System");
			superuserRole.setVerifiedFlag('Y');
			superuserRole.setVerifiedTime(new Date());
			superuserRole.setPostedTime(new Date());
			superuserRole.setName(ERole.ROLE_SUPERUSER.toString());
			roleList.add(superuserRole);
		}
		roleRepository.saveAll(roleList);
	}

	private void initWorkClasses() {
		if (workclassRepo.findAll().size() < 1) {
			Optional<Role> role = roleRepository.findByName("ROLE_SUPERUSER");
			Role role1 = role.get();
			Workclass workclass = new Workclass();
			workclass.setEntityId("001");
			workclass.setWorkClass("SUPERUSER");
			workclass.setPostedBy("System");
			workclass.setPostedFlag('Y');
			workclass.setVerifiedBy("System");
			workclass.setVerifiedFlag('Y');
			workclass.setVerifiedTime(new Date());
			workclass.setPostedTime(new Date());
			workclass.setRoleId(role1.getId());
			workclass.setPrivileges(initAuth.getAllPriviledges());
			workclassRepo.save(workclass);
			List<Privilege> privilege = workclass.getPrivileges();
			for (int i = 0; i < privilege.size(); i++) {
				Privilege privilege1 = privilege.get(i);
				List<Basicactions> basicactionsList = initAuth.getAllBasicActions();
				for (int j = 0; j < basicactionsList.size(); j++) {
					Basicactions basicactions = basicactionsList.get(j);
					Basicactions basicactions1 = new Basicactions();
					basicactions1.setCode(basicactions.getCode());
					basicactions1.setName(basicactions.getName());
					basicactions1.setSelected(basicactions.isSelected());
					basicactions1.setPostedBy("System");
					basicactions1.setPostedFlag('Y');
					basicactionsService.addBasicactions(basicactionsList, privilege1.getId(), workclass.getId());
				}
			}
		}
	}

	private void initSuperUser() throws MessagingException {
		if (!userRepository.existsByUsername(superUserUserName)) {
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName(ERole.ROLE_SUPERUSER.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			Users user = new Users();
			user.setFirstName(superUserFirstName);
			user.setLastName(superUserLastName);
			user.setEmail(superUserEmail);
			user.setPhoneNo(superUserPhone);
			user.setSolCode(superUserSolCode);
			user.setCreatedOn(new Date());
			user.setModifiedBy("");
			user.setUsername(superUserUserName);
			user.setRoles(roles);
			user.setPostedTime(new Date());
			user.setPostedFlag('Y');
			user.setPostedBy("System");
			user.setIsAcctLocked(false);
			user.setWorkclassFk(workclassRepo.findByName("SUPERUSER").get().getId());
			user.setFirstLogin('Y');
			user.setSolCode("001");
			user.setEntityId("001");
			user.setPassword(encoder.encode(superUserPassword));
			userRepository.save(user);
			String mailMessage = "Dear " + user.getFirstName() + " your account has been successfully created using username " + user.getUsername()
					+ " and password " + superUserPassword + " Login in to change your password.";
//			mailService.sendEmail(user.getEmail(), mailMessage, "Account Successfully Created");
			System.out.println("Account Successfully Created");
			Mailparams mailsample = new Mailparams();
			mailsample.setEmail(user.getEmail());
			mailsample.setSubject("Account Successfully Created");
			mailsample.setMessage(mailMessage);
		} else {
			Users user = userRepository.findByUsername(superUserUserName).get();
			if (user.getWorkclassFk() == null || !workclassRepo.existsById(user.getWorkclassFk())) {
				user.setWorkclassFk(workclassRepo.findByName("SUPERUSER").get().getId());
				userRepository.save(user);
			}
		}
	}

	private void initOtherUsers() {
		List<Users> users = userRepository.allWithoutWorkclass();
		for (Users user : users) {
			if (user.getWorkclassFk() == null || !workclassRepo.existsById(user.getWorkclassFk())) {
				user.setWorkclassFk(workclassRepo.findByName(NONE).get().getId());
				userRepository.save(user);
			}
		}

		users = userRepository.allWithoutRoles();
		for (Users user : users) {
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName(ERole.ROLE_NONE.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			user.setRoles(roles);
			userRepository.save(user);
		}
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			initEntity();
			initRoles();
			initWorkClasses();
			initSuperUser();
//            initOtherUsers();
			System.out.println("EMTECH TICKETING SYSTEM INITIALIZED SUCCESSFULLY AT " + new Date());
		};
	}
}