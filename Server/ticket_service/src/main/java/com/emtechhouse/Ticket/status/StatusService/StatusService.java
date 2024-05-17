package com.emtechhouse.Ticket.status.StatusService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.priority.PriorityRepository.PriorityRepository;
import com.emtechhouse.Ticket.status.StatusModel.Status;
import com.emtechhouse.Ticket.status.StatusRepository.StatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusService {
    @Autowired
    StatusRepository statusRepository;

    public Status addStatus(Status status) {
        try {
            return statusRepository.save(status);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public Status updateStatus(Status status) {
        try {
            return statusRepository.save(status);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Status findById(Long id){
        try{
            return statusRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
