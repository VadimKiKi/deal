package ru.taratonov.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taratonov.deal.dto.ErrorDTO;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/deal/admin/application")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Managing applications using db")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/{applicationId}")
    @Operation(summary = "Get application by id", description = "Allows to get application by id")
    @ApiResponse(
            responseCode = "200",
            description = "Application received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public Application getApplication(@Parameter(description = "Id of the application", required = true)
                                      @PathVariable("applicationId") Long id){
        return adminService.getApplicationById(id);
    }

    @GetMapping
    @Operation(summary = "Get all applications", description = "Allows to get all application from db")
    @ApiResponse(
            responseCode = "200",
            description = "Applications received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    public List<Application> getAllApplications(){
        return adminService.getAllApplications();
    }
}
