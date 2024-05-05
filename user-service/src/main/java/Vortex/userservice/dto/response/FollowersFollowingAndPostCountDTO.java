package Vortex.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowersFollowingAndPostCountDTO {

    private int followingCount;
    private int followersCount;
    private int postCount;
}
