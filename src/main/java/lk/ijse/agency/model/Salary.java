package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Salary {
    private String salaryId;
    private String empId;
    private String name;
    private String month;
    private double amount;
    private String date;

}
