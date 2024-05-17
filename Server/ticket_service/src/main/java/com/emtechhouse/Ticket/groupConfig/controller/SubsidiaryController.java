package com.emtechhouse.Ticket.groupConfig.controller;

import com.emtechhouse.Ticket.Utils.EntityResponse;
import com.emtechhouse.Ticket.groupConfig.model.Subsidiary;
import com.emtechhouse.Ticket.groupConfig.service.SubsidiaryService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subsidiary")
public class SubsidiaryController {
    @Autowired
    private final SubsidiaryService subsidiaryService;

    public SubsidiaryController(SubsidiaryService subsidiaryService) {
        this.subsidiaryService = subsidiaryService;
    }
    @PostMapping("/create")
    public EntityResponse create (@RequestBody Subsidiary subsidiary){
        return subsidiaryService.create(subsidiary);
    }
}
