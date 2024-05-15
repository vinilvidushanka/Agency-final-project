package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerTm {
    private String id;
    private String name;
    private String shopName;
    private String contact;
    private String address;
    private String routeId;

}
