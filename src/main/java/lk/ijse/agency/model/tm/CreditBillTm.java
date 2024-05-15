package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditBillTm {
    private String billId;
    private String cusId;
    private String routeId;
    private double amount;
    private String date;

}
