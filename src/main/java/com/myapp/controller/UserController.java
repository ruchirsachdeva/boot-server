package com.myapp.controller;

import com.myapp.domain.User;
import com.myapp.dto.ErrorResponse;
import com.myapp.dto.UserDTO;
import com.myapp.dto.UserParams;
import com.myapp.service.SocialUserService;
import com.myapp.service.UserService;
import com.myapp.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
@RequestMapping("/api/users")
public class UserController {

    private static final Integer DEFAULT_PAGE_SIZE = 5;

    private final UserService userService;
    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private SocialUserService socialUserService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDTO> list(@RequestParam(value = "page", required = false) @Nullable Integer page,
                              @RequestParam(value = "size", required = false) @Nullable Integer size) {
        final PageRequest pageable = new PageRequest(
                Optional.ofNullable(page).orElse(1) - 1,
                Optional.ofNullable(size).orElse(DEFAULT_PAGE_SIZE));
        return userService.findAll(pageable);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(method = RequestMethod.POST)
    public User create(@Valid @RequestBody UserParams params, WebRequest request) {
        User user = userService.create(params);
        //providerSignInUtils.doPostSignUp(user.getUsername(), request);

        socialUserService.postUserSignUp(params.getToken());

        //   Optional<String> token = MyUtil.logInUser(user);
        return user;
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(method = RequestMethod.GET, path = "{id:\\d+}")
    public UserDTO show(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.findOne(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(method = RequestMethod.GET, path = "/me")
    public UserDTO showMe() {
        return userService.findMe()
                .orElseThrow(() -> new AccessDeniedException(""));
    }

    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com/"})
    @RequestMapping(method = RequestMethod.PATCH, path = "/me")
    public void updateMe(@Valid @RequestBody UserParams params) {
        userService.updateMe(params);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleValidationException(DataIntegrityViolationException e) {
        return new ErrorResponse("email_already_taken", "This email is already taken.");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No user")
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFound() {
    }


}
