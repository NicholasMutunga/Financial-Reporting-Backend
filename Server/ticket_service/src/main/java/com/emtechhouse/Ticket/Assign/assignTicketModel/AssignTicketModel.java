package com.emtechhouse.Ticket.Assign.assignTicketModel;

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
public class AssignTicketModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String assignee;

    @Column(length = 6, nullable = false)
    private String entityId;

    @Column(nullable = false)
    private String status;

    @Column(length = 15, nullable = false)
    private String ticketsCode;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;


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
