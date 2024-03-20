package Vortex.postservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainPostViewDTO {
    private int nextPageIndex;
    private List<AllPostViewDTO> postViewList;
}
