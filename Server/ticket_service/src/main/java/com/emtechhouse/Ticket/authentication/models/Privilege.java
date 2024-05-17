package com.emtechhouse.Ticket.authentication.models;

import com.emtechhouse.Ticket.authentication.basicActions.Basicactions;
import com.emtechhouse.Ticket.authentication.workclass.Workclass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
//@EqualsAndHashCode(exclude = {"basicactions"})
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    private boolean selected;
    private String code;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "workclassFk")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Workclass workclass;

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "privilege")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Basicactions> basicactions;

}
