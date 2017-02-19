package com.myapp.controller;

import com.myapp.domain.User;
import com.myapp.dto.UserParams;
import com.myapp.service.UserParamsValidator;
import com.myapp.service.UserService;
import com.myapp.util.MyUtil;
import lombok.Value;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

@RestController
// TODO : not in use, to be fixed in future when restricting only backend to connect to social and not
// TODO: expect frontend to send accesstoken.
public class SignUpController {
	
	private final Log log = LogFactory.getLog(SignUpController.class);
	
	private UserService userService;
	private ProviderSignInUtils providerSignInUtils;
	private UserParamsValidator signupFormValidator;
	
	@Autowired
	public SignUpController(UserService userService,
							ProviderSignInUtils providerSignInUtils, UserParamsValidator userParamsValidator) {
		this.userService = userService;
		this.providerSignInUtils = providerSignInUtils;
		this.signupFormValidator = userParamsValidator;
	}


	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	//THIS METHOD IS THE CALLBACK METHOD AFTER SIGNING IN Using facebook
	public AuthResponse signup(WebRequest request, RedirectAttributes redirectAttributes) {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		UserParams signupForm=null;
		if(connection!= null) {
			signupForm = UserParams.fromConnection(connection);
			Set<ConstraintViolation<UserParams>> validate = signupFormValidator.validate(signupForm);
			if (validate.isEmpty()) {
				User user = userService.signup(signupForm);
				providerSignInUtils.doPostSignUp(user.getUsername(), request);
				//todo MOVE LOGIN USER LOGIC TO THE RETURN METHOD CALLED FROM AUNGULAR CLIENT.
				// THE CURRENT METHOD IS THE CALLBACK METHOD AFTER SIGNING UP
				Optional<String> token = MyUtil.logInUser(user);
				return new AuthResponse(token.get());
			}
		}
		return null;
		
	}

	/**
@RequestMapping(value = "/signup", method = RequestMethod.GET)
public UserParams signup(WebRequest request, RedirectAttributes redirectAttributes) {
	Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
	return UserParams.fromConnection(connection);

}

 **/


	@Value
	private static final class AuthResponse {
		private final String token;
	}
	/**
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm,
			BindingResult result, WebRequest request, RedirectAttributes redirectAttributes) {

		if (result.hasErrors())
			return "signup";

		User user = userService.signup(signupForm);
		providerSignInUtils.doPostSignUp(user.getUsername(), request);

		MyUtil.flash(redirectAttributes, "success", "signupSuccess");
		
		return "redirect:/";

	}**/

}
