package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.User;
import com.example.syworks_dmo2595.repository.UserRepository;
import com.example.syworks_dmo2595.service.dto.request.UserServiceLoginRequest;
import com.example.syworks_dmo2595.service.dto.request.UserServiceSignupRequest;
import com.example.syworks_dmo2595.service.dto.response.UserServiceLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;


    @Override
    public void signupUser(UserServiceSignupRequest request) {
        Boolean isExistUserId = userRepository.existsByLoginId(request.getLoginId());

        if (isExistUserId) throw new RuntimeException();
        User user;
        try {
            user = userRepository.save(User.builder()
                            .loginId(request.getLoginId())
                            .userName(request.getUserName())
                            .password(request.getPassword())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }


    @Override
    public UserServiceLoginResponse loginUser(UserServiceLoginRequest request) {
        Boolean isExistUserId = userRepository.existsByLoginId(request.getLoginId());
        Boolean isExistPassword = userRepository.existsByPassword(request.getPassword());

        User user = userRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword());
        System.out.println("로그인아이디 : " + user.getLoginId());
        System.out.println("사용자 이름 : " + user.getUserName());

        return UserServiceLoginResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .build();
    }



}
