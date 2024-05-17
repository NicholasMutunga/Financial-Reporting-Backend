package com.emtechhouse.Ticket.escalate.EscalateService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.category.CategoryRepository.CategoryRepository;
import com.emtechhouse.Ticket.escalate.EscalateModel.Escalate;
import com.emtechhouse.Ticket.escalate.EscalateRepository.EscalateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EscalateService {
    @Autowired
    EscalateRepository escalateRepository;

    public Escalate addIssue(Escalate escalate) {
        try {
            return escalateRepository.save(escalate);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public Escalate updateIssue(Escalate escalate) {
        try {
            return escalateRepository.save(escalate);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Escalate findById(Long id){
        try{
            return escalateRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
