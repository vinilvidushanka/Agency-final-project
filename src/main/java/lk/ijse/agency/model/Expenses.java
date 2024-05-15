package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Expenses {
    private String code;
    private String vanId;
    private double amount;
    private String description;
    private String date;

}
