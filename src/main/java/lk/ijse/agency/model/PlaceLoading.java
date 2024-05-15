package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class PlaceLoading {
    private Loading loading;
    private List<LoadingDetail> ldList;
    //private List<Loading> itList;
}
