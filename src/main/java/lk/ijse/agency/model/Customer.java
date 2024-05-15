package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    private String id;
    private String name;
    private String shopName;
    private String contact;
    private String address;
    private String routeId;

}
