package Vortex.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowingDTO {
    private String userID;
    private String userEmail;
    private List<String> followingUserEmailList;
}
