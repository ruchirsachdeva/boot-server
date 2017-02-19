package com.myapp.service;

import com.myapp.domain.User;
import com.myapp.dto.UserDTO;
import com.myapp.dto.UserParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> findOne(Long id);

    Optional<UserDTO> findMe();

    Page<UserDTO> findAll(PageRequest pageable);

    User create(UserParams params);

    User update(User user, UserParams params);

    User updateMe(UserParams params);

    void update(long userId, UserEditForm userEditForm);

    User signup(UserParams signupForm);


}
