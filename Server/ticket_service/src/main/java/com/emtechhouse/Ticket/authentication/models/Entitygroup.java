package com.emtechhouse.Ticket.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Entitygroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 3, nullable = false, unique = true)
    @JsonProperty(required = true)
    private String entityCode;
    @Column(length = 200, nullable = false, unique = false)
    private String entityDescription;
    @Column(length = 20, nullable = false, unique = false)
    private  String entityLocation;
    @Column(length = 100, nullable = false,unique = false)
    private String entityEmail;
    @Column(length = 12, nullable = false, unique = false)
    private String entityPhoneNumber;

    //*****************Operational Audit *********************
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
    private Date verifiedTime;
    private String deletedBy;
    private Character deletedFlag = 'N';
    private Date deletedTime;
}
