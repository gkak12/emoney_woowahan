package com.emoney.api;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.service.EmoneyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RequestMapping("/emoney")
@RequiredArgsConstructor
public class EmoneyController {

    private final EmoneyService emoneyService;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping({"/usage", "/deduct"})
    public ResponseEntity<Void> deductEmoney(@RequestBody @Valid RequestEmoneyDeductDto emoneyDeductDto){
        emoneyService.deductEmoney(emoneyDeductDto);
        return ResponseEntity.ok().build();
    }
}
