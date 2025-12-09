package chill_logistics.firm_server.presentation;

import chill_logistics.firm_server.application.service.FirmService;
import chill_logistics.firm_server.presentation.dto.request.FirmCreateRequestV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/firms")
public class FirmController {

    private final FirmService firmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MASTER')")
    public void createFirm(@RequestBody @Valid FirmCreateRequestV1 createRequest) {
        firmService.createFirm(createRequest.to());
    }




}
