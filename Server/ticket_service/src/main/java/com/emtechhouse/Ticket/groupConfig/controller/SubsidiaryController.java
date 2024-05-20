package com.emtechhouse.Ticket.groupConfig.controller;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.Utils.CONSTANTS;
import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.EntityRequestContext;
import com.emtechhouse.Ticket.Utils.HttpInterceptor.UserRequestContext;
import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import com.emtechhouse.Ticket.groupConfig.repo.SubsidiaryRepo;
import com.emtechhouse.Ticket.groupConfig.service.SubsidiaryService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1/subsidiary")
public class SubsidiaryController {
    @Autowired
    private Subsidiary subsidiary;
    @Autowired
    private SubsidiaryRepo subsidiaryRepo;
    @Autowired
    private SubsidiaryService subsidiaryService;

    @PostMapping("/add")
    public ResponseEntity<?> addTicketAssignee(@RequestBody Subsidiary subsidiary) {
        try {
            if (UserRequestContext.getCurrentUser().isEmpty()) {
                EntityResponse response = new EntityResponse();
                response.setMessage("User Name not present in the Request Header");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setEntity("");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else
            {
                if (EntityRequestContext.getCurrentEntityId().isEmpty()) {
                    EntityResponse response = new EntityResponse();
                    response.setMessage("Entity not present in the Request Header");
                    response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setEntity("");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                else
                {
                    Optional<Subsidiary> checkSubsidiary = subsidiaryRepo.findByCompanyName(subsidiary.getCompanyName());
                    if (checkSubsidiary.isPresent()) {
                        EntityResponse response = new EntityResponse();
                        response.setMessage("Subsidiary with Company Name " + checkSubsidiary.get().getCompanyName() + " ALREADY CREATED ON " + checkSubsidiary.get().getPostedTime());
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                        response.setEntity("");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        subsidiary.setPostedBy(UserRequestContext.getCurrentUser());
                        subsidiary.setEntityId(EntityRequestContext.getCurrentEntityId());
                        subsidiary.setPostedFlag('Y');
                        subsidiary.setEntityId("001");
                        subsidiary.setModifiedBy("System");
                        subsidiary.setPostedTime(new Date());
                        Subsidiary newSubsidiary= subsidiaryService.addSubsiiary(subsidiary);
                        EntityResponse response = new EntityResponse();
                        System.out.println("Subsidiary with Company Name " + newSubsidiary.getCompanyName() +  " CREATED SUCCESSFULLY AT " + newSubsidiary.getPostedTime());
                        response.setMessage("Subsidiary with Company Name " + newSubsidiary.getCompanyName() +  " CREATED SUCCESSFULLY AT " + newSubsidiary.getPostedTime());
                        response.setStatusCode(HttpStatus.CREATED.value());
                        response.setEntity(newSubsidiary);
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
