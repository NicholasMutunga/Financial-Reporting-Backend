package com.emtechhouse.Ticket.groupConfig.service;

import com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel.TicketAssignee;
import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import com.emtechhouse.Ticket.groupConfig.repo.SubsidiaryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.Optional;

@Service
@Slf4j
public class SubsidiaryService {
    @Autowired
     SubsidiaryRepo subsidiaryRepo;


    public Subsidiary addSubsidiary(Subsidiary newSubsidiary) {
        try {
            return subsidiaryRepo.save(newSubsidiary);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
    public Subsidiary updateSubsidiary(Subsidiary subsidiary) {
        try {
            return subsidiaryRepo.save(subsidiary);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }
}
