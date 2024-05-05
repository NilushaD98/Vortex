package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDeliverDetailsDTO {

    private String email;
    private String address;
    private String nic;
}
