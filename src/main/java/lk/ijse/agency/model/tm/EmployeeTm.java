package lk.ijse.agency.model.tm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeTm {
    private String id;
    private String name;
    private String nic;
    private String address;
    private int contact;
    private String vanId;
}