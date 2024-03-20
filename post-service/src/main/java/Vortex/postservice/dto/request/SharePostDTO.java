package Vortex.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SharePostDTO {
    private String sharedPostID;
    private String sharedUserEmail;
    private String sharedComment;
    private Date sharedTime;
}
