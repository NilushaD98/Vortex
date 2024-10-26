package Vortex.marketplace_service.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AllItemReviewDTO {

    private double itemRatings;
    private List<ItemReviewDTO> itemReviewDTOList;
}
