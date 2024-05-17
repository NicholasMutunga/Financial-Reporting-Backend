package com.emtechhouse.Ticket.authentication.models;

import lombok.*;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SignupRequest {
    private Long sn;
    @NonNull
    @Column(length = 20)
    private String username;
    @NonNull
    @Column(length = 50)
    private String email;
    private String roleFk;
    //    @Password
    private String password;
    @NonNull
    @Column(length = 20)
    private String firstName;
    @NonNull
    @Column(length = 20)
    private String lastName;
    @NonNull
    @Column(length = 13)
    private String phoneNo;
    @NonNull
    @Column(length = 6)
    private String solCode;
    @NonNull
    @Column(length = 6)
    private String entityId;
    private String isTeller = "No";
    private String workclassFk;
    private String memberCode;
    private  String onBoardingMethod;
}
