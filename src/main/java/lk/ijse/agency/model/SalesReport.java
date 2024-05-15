package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SalesReport {
    private String billCode;
    private String itemCode;
    private String itemName;
    private int qty;
    private int freeQty;
    private String date;
    private String vanId;
    private double amount;
    private double unitPrice;
}
