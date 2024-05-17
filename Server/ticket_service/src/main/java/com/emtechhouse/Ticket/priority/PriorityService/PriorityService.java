package com.emtechhouse.Ticket.priority.PriorityService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.priority.PriorityRepository.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriorityService {

    @Autowired
    PriorityRepository priorityRepository;

    public Priority addPriority(Priority priority) {
        try {
            return priorityRepository.save(priority);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public Priority updatePriority(Priority priority) {
        try {
            return priorityRepository.save(priority);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Priority findById(Long id){
        try{
            return priorityRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
