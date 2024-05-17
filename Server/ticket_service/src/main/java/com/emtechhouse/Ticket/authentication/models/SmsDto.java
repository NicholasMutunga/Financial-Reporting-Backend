package com.emtechhouse.Ticket.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsDto {
    private String msisdn;
    private String text;
}
