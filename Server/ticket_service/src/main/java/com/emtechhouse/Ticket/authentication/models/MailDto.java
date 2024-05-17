package com.emtechhouse.Ticket.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MailDto {
    private String text;
    private String mailTo;
    private String subject;
    private String phoneNumber;
}
