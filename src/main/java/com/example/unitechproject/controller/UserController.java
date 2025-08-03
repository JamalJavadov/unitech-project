package com.example.unitechproject.controller;

import com.example.unitechproject.model.dto.userDto.UserResponseDto;
import com.example.unitechproject.model.dto.userDto.UserUpdateDto;
import com.example.unitechproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserUpdateDto userUpdateDto, Authentication authentication){
        return ResponseEntity.ok(userService.update(userUpdateDto,authentication.getName()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getProfile(Authentication authentication){
        return ResponseEntity.ok(userService.profile(authentication.getName()));
    }


}
