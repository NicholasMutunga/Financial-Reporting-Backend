package com.emtechhouse.Ticket.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Mailparams {
    private String email;
    private String subject;
    private String message;
}
