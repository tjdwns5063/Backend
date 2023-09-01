package Lingtning.new_match42.controller;

import Lingtning.new_match42.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "firebasetest", description = "테스트 API")
public class FirebaseController {
    private final FirebaseService firebaseService;

    @Autowired
    public FirebaseController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/firebasetest")
    @Operation(summary = "Hello!", description = "처음으로 만든 API", responses = {
            @ApiResponse(responseCode = "200", description = "야호! 성공!!!")
    })
    public List<Map<String, Object>> helloFirebase() {
        // FirebaseService를 통한 작업 수행
        List<Map<String, Object>> firebaseDataList = firebaseService.readAllDataFromRoomsCollection();
        return firebaseDataList; // 모든 Firebase 데이터를 반환
    }
}
