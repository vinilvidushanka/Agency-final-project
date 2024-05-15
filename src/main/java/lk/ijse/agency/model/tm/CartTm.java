package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CartTm extends OrderTm {
    private String itemCode;
    private String name;
    private int qty;
    private String orderId;
    private String date;
}
