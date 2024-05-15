package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleCartTm extends OrderTm {
    private String billCode;
    private String itemCode;
    private String itemName;
    private int qty;
    private double unitPrice;
    private double amount;
    private String date;
    private String vanId;
    private int freeQty;
}
