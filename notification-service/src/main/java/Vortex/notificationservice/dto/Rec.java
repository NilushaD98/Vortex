package Vortex.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rec {

    private String title;
    private List<String> ingredients;
    private List<String> instructions;
}
