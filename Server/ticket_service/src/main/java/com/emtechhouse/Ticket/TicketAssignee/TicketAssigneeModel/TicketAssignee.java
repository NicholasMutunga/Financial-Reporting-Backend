package com.emtechhouse.Ticket.TicketAssignee.TicketAssigneeModel;

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
public class TicketAssignee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(length = 15, nullable = false, unique = true)
//    private String ticketAssigneeCode;

    @Column(length = 15, nullable = false)
    private String names;

    @Column(length = 15, nullable = false, unique = true)
    private String nationalId;

    @Column
    private String designation;

    @Column(length = 12, nullable = false)
    private String phoneNumber;

    @Column(length = 6)
    private String avaya;

    @Column(length = 40, nullable = false, unique = true)
    private String email;

    @Column
    private String entityId;


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
    ;
    private Date verifiedTime;
    private String deletedBy;
    private Character deletedFlag = 'N';
    ;
    private Date deletedTime;
}
