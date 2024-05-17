package com.emtechhouse.Ticket.authentication.basicActions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BasicActionRequest {
    private Long id;
    private Long privilegeId;
    private Long workclassId;
    private List<Basicactions> basicactions;
}
