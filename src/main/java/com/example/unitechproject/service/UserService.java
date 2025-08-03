package com.example.unitechproject.service;

import com.example.unitechproject.model.dto.userDto.UserUpdateDto;
import com.example.unitechproject.model.entity.User;
import com.example.unitechproject.model.mapper.UserMapper;
import com.example.unitechproject.repository.UserRepository;
import com.example.unitechproject.model.dto.userDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto profile(String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        return userMapper.toDto(user);
    }

    public UserResponseDto update(UserUpdateDto userUpdateDto, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        user = userMapper.toDto(userUpdateDto,user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

}
