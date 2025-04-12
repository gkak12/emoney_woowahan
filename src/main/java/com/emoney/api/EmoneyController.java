package com.emoney.api;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySaveDto;
import com.emoney.service.EmoneyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/emoney")
@RequiredArgsConstructor
public class EmoneyController {

    private final EmoneyService emoneyService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/save.do")
    public ResponseEntity<Void> saveEmoney(@RequestBody @Valid RequestEmoneySaveDto emoneySaveDto){
        emoneyService.saveEmoney(emoneySaveDto);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping({"/usage.do", "/deduct.do"})
    public ResponseEntity<Void> deductEmoney(@RequestBody @Valid RequestEmoneyDeductDto emoneyDeductDto){
        emoneyService.deductEmoney(emoneyDeductDto);
        return ResponseEntity.ok().build();
    }
}
