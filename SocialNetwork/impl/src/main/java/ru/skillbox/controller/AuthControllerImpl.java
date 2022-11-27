package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.exception.EmailNotFoundException;
import ru.skillbox.exception.UserIsAlreadyRegisteredException;
import ru.skillbox.model.AuthController;
import ru.skillbox.request.LoginRequest;
import ru.skillbox.request.PasswordRecoveryRequest;
import ru.skillbox.request.RegistrationRequest;
import ru.skillbox.response.LoginResponse;
import ru.skillbox.response.PasswordRecoveryResponse;
import ru.skillbox.response.RegistrationResponse;
import ru.skillbox.service.AuthService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        try {
            log.info("{} is login in", request.getEmail());
            return ResponseEntity.ok(authService.login(request));
        } catch (UsernameNotFoundException e) {
            log.error("login attempt for {}", request.getEmail());
            return ResponseEntity.badRequest().body(LoginResponse.getBadResponse());
        }
    }

    @Override
    public ResponseEntity<RegistrationResponse> registration(RegistrationRequest request) {
        try {
            log.info("{} registered", request.getEmail());
            return ResponseEntity.ok(authService.registration(request));
        } catch (UserIsAlreadyRegisteredException e) {
            return ResponseEntity.badRequest().body(RegistrationResponse.getBadResponse());
        }
    }

    @Override
    public ResponseEntity<PasswordRecoveryResponse> passwordRecovery(PasswordRecoveryRequest request) {
        try {
            return ResponseEntity.ok(authService.passwordRecovery(request));
        } catch (EmailNotFoundException e) {
            return ResponseEntity.badRequest().body(PasswordRecoveryResponse.getBadResponse());
        }
    }

    @Override
    public String logout() {
        return "logout";
    }

    @Override
    public ResponseEntity<String> captcha() {
        return null;//TODO: captcha
    }
}