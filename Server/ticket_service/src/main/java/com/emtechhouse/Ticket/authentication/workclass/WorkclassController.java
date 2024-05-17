package com.emtechhouse.Ticket.authentication.workclass;

import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.authentication.Role.Role;
import com.emtechhouse.Ticket.authentication.Role.RoleRepository;
import com.emtechhouse.Ticket.authentication.models.Privilege;
import com.emtechhouse.Ticket.authentication.repositories.PrivilegeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("system/ticketing/workclass")
@Slf4j
public class WorkclassController {
    private final WorkclassRepo workclassRepo;
    private final WorkclassService workclassService;
    private final PrivilegeRepo privilegeRepo;
    private final RoleRepository roleRepository;

    public WorkclassController(WorkclassRepo workclassRepo, WorkclassService workclassService, PrivilegeRepo privilegeRepo, RoleRepository roleRepository) {
        this.workclassRepo = workclassRepo;
        this.workclassService = workclassService;
        this.privilegeRepo = privilegeRepo;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAccessgroup(@RequestBody WorkclassRequest workclassRequest) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
//                    Check if exist
                    Optional<Role> role = roleRepository.findById(workclassRequest.getRoleId());
                    if (role.isPresent()){
                        Workclass workclass = new Workclass();
                        workclass.setPostedBy(UserRequestContext.getCurrentUser());
                        workclass.setEntityId(EntityRequestContext.getCurrentEntityId());
                        workclass.setPostedFlag('Y');
                        workclass.setPostedTime(new Date());
                        workclass.setRoleId(role.get().getId());
                        workclass.setWorkClass(workclassRequest.getWorkClass());
                        workclass.setPrivileges(workclassRequest.getPrivileges());
                        Workclass newWorkclass = workclassRepo.save(workclass);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("WORK CLASS WITH CLASS NAME " + workclass.getWorkClass() + " CREATED SUCCESSFULLY AT " + workclass.getPostedTime() );
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newWorkclass);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }else{
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Role is not present");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAccessgroups() {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    List<Workclass> workclass = workclassRepo.findByEntityIdAndDeletedFlag(EntityRequestContext.getCurrentEntityId(), 'N');
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(workclass);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getAccessgroupById(@PathVariable("id") Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Workclass workclass = workclassService.findById(id);
                    EntityResponse response = new EntityResponse();
                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setEntity(workclass);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/find/privilege/by/workclass/{id}")
    public ResponseEntity<?> getPrevileges(@PathVariable("id") Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Optional<Workclass> workclass = workclassRepo.findById(id);
                    if (workclass.isPresent()){
                        List<Privilege> privileges = privilegeRepo.findByWorkclassAndSelected(workclass.get(),true);
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(privileges);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }else{
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Workclass Not available");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @GetMapping("/find/workclass/by/role/{id}")
    public ResponseEntity<?> getWorkCLassByrole(@PathVariable("id") Long id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    List<Workclass> workclass = workclassRepo.findByRoleIdAndDeletedFlag(id, 'N');
                    if (workclass.size()>0){
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(workclass);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }else{
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Workclass Not available");
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }

                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }



    @PutMapping("/modify")
    public ResponseEntity<?> updateAccessgroup(@RequestBody Workclass workclass) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    workclass.setModifiedBy(UserRequestContext.getCurrentUser());
                    workclass.setEntityId(EntityRequestContext.getCurrentEntityId());
                    Optional<Workclass> workclass1 = workclassRepo.findById(workclass.getId());
                    if (workclass1.isPresent()) {
                        workclass.setId(workclass.getId());
                        workclass.setPostedTime(workclass1.get().getPostedTime());
                        workclass.setPostedFlag('Y');
                        workclass.setPostedBy(workclass1.get().getPostedBy());
                        workclass.setModifiedFlag('Y');
                        workclass.setVerifiedFlag('N');
                        workclass.setModifiedTime(new Date());
                        workclass.setModifiedBy(workclass.getModifiedBy());
                        workclassRepo.save(workclass);
                        EntityResponse response = new EntityResponse();
                        response.setMessage("WORK CLAS WITH NAME " + workclass.getWorkClass() + " MODIFIED SUCCESSFULLY AT " + workclass.getModifiedTime());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(workclass);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verify(@PathVariable String id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Optional<Workclass> workclass1 = workclassRepo.findById(Long.parseLong(id));
                    if (workclass1.isPresent()) {
                        Workclass workclass = workclass1.get();
                        // Check Maker Checker
                        if (workclass.getPostedBy().equalsIgnoreCase(UserRequestContext.getCurrentUser())){
                            EntityResponse response = new EntityResponse();
                            response.setMessage("You Can Not Verify What you initiated");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                            response.setEntity("");
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }else{
                            workclass.setVerifiedFlag('Y');
                            workclass.setVerifiedTime(new Date());
                            workclass.setVerifiedBy(UserRequestContext.getCurrentUser());
                            workclassRepo.save(workclass);
                            EntityResponse response = new EntityResponse();
                            response.setMessage("WORK CLASS WITH CLASS NAME " + workclass.getWorkClass() + " VERIFIED SUCCESSFULLY AT " + workclass.getVerifiedTime() );
                            response.setStatusCode(HttpStatus.OK.value());
                            response.setEntity(workclass);
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccessgroup(@PathVariable String id) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    Optional<Workclass> workclass1 = workclassRepo.findById(Long.parseLong(id));
                    if (workclass1.isPresent()) {
                        Workclass workclass = workclass1.get();
                        workclass.setDeletedFlag('Y');
                        workclass.setDeletedTime(new Date());
                        workclass.setDeletedBy(UserRequestContext.getCurrentUser());
                        workclassRepo.save(workclass);
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setEntity(workclass);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        EntityResponse response = new EntityResponse();
                        response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
}
