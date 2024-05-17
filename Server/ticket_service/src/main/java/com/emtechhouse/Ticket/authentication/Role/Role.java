package com.emtechhouse.Ticket.authentication.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 6, nullable = false)
    @JsonIgnore
    private String entityId;
    @Column(length = 20, nullable = false, unique = true)
    private String name;

//    //    TODO: One Role has many workclass
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
//    @JsonIgnore
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<Workclass> workclass = new ArrayList<>();

//    //    TODO: One workclass has many basic actions
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workclass")
//    @JsonIgnore
//    @ToString.Exclude
//    private List<Basicactions> basicactions;

    //*****************Operational Audit *********************
    @Column(length = 30, nullable = false)
//    @JsonIgnore
    private String postedBy;
    @Column(nullable = false)
//    @JsonIgnore
    private Character postedFlag = 'Y';
    //    @Column(nullable = false)
//    @JsonIgnore
    private Date postedTime;
    //    @JsonIgnore
    private String modifiedBy;
    //    @JsonIgnore
    private Character modifiedFlag = 'N';
    //    @JsonIgnore
    private Date modifiedTime;
    //    @JsonIgnore
    private String verifiedBy;
    //    @JsonIgnore
    private Character verifiedFlag = 'N';
    //    @JsonIgnore
    private Date verifiedTime;
    //    @JsonIgnore
    private String deletedBy;
    //    @JsonIgnore
    private Character deletedFlag = 'N';
    //    @JsonIgnore
    private Date deletedTime;
}
