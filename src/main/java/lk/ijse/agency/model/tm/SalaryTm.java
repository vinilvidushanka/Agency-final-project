package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SalaryTm {
    private String salaryId;
    private String empId;
    private String name;
    private String month;
    private double amount;
    private String date;

}
