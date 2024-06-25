package Vortex.notificationservice.controller;

import Vortex.notificationservice.dto.NotificationDTO;
import Vortex.notificationservice.service.NotificationService;
import Vortex.notificationservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("get_all_notification_by_userEmail")
    public ResponseEntity<StandardResponse> getAllNotificationByUserEmail(@RequestParam("userEmail") String userEmail){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Notifications ",notificationService.getAllNotification(userEmail)),HttpStatus.OK
        );
    }
}
