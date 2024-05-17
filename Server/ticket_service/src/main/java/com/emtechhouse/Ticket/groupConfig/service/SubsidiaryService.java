package com.emtechhouse.Ticket.groupConfig.service;

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


    public SubsidiaryService(SubsidiaryRepo subsidiaryRepo) {
        this.subsidiaryRepo = subsidiaryRepo;
    }

    public EntityResponse<Subsidiary> create(Subsidiary subsidiary) {
        EntityResponse<Subsidiary> response =new EntityResponse<>();
        try{
            Optional<Subsidiary> checkSubsidiary = subsidiaryRepo.findByCompanyName(subsidiary.getCompanyName());
            if (checkSubsidiary.isPresent()){
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setMessage("Subsidiary with name " + subsidiary.getCompanyName() +"already exists");

            }else {
                Subsidiary saved = subsidiaryRepo.save(subsidiary);
                response.setMessage("Created successfully");
                response.setStatusCode(HttpStatus.CREATED.value());
                response.setEntity(saved);

                subsidiaryRepo.save(saved);
            }

        }catch (Exception e){
            log.error("Error{}" +e);
        }
        return response;

}}
