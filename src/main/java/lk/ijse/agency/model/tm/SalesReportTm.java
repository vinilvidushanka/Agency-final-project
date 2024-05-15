package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SalesReportTm {
    private String billCode;
    private String itemCode;
    private int qty;
    private int freeQty;
    private String date;
    private String itemName;
    private String vanId;
    private double amount;
    private double unitPrice;
}
