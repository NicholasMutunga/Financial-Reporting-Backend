package com.emtechhouse.Ticket.groupConfig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Subsidiary")
public class Subsidiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String subsidiaryCode;
    private String entityId;

   // @DateTimeFormat (pattern = "dd/mm/yyyy")
   @Column(length = 30, nullable = false)
   private String postedBy;
    @Column(nullable = false)
    private Character postedFlag = 'Y';
    @Column(nullable = false)
    private Date postedTime;
    private String modifiedBy;
    private Character modifiedFlag = 'N';
    private Date modifiedTime;
    private String verifiedBy;
    private Character verifiedFlag = 'N';
    ;
    private Date verifiedTime;
    private String deletedBy;
    private Character deletedFlag = 'N';
    ;
    private Date deletedTime;

}
