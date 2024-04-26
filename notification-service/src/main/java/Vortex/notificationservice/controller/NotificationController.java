package Vortex.notificationservice.controller;

import Vortex.notificationservice.dto.NotificationDTO;
import Vortex.notificationservice.service.NotificationService;
import Vortex.notificationservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vortexnotificationservice/api/v1/")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("add")
    public ResponseEntity<StandardResponse> add(@RequestBody NotificationDTO notificationDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Notification Added Status",notificationService.add(notificationDTO)), HttpStatus.OK
        );
    }

}
