package lk.ijse.agency.model.tm;

import lk.ijse.agency.model.tm.EmployeeTm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpensesTm extends EmployeeTm {
    private String code;
    private String vanId;
    private double amount;
    private String description;
    private String date;

}
