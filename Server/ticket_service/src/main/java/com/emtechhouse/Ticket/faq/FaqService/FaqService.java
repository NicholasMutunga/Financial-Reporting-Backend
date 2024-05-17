package com.emtechhouse.Ticket.faq.FaqService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.faq.FaqModel.Faq;
import com.emtechhouse.Ticket.faq.FaqRepository.FaqRepository;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import com.emtechhouse.Ticket.priority.PriorityRepository.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FaqService {
    @Autowired
    FaqRepository faqRepository;

    public Faq addFaq(Faq faq) {
        try {
            return faqRepository.save(faq);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public Faq updateFaq(Faq faq) {
        try {
            return faqRepository.save(faq);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Faq findById(Long id){
        try{
            return faqRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
