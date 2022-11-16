package com.example.itshop.services.client;

import com.example.itshop.dto.common.AuthEmailDto;
import com.example.itshop.dto.client.request.CreateUserClientReqDto;
import com.example.itshop.dto.client.request.UserLoginClientReqDto;
import com.example.itshop.dto.client.request.UpdateUserClientReqDto;
import com.example.itshop.dto.client.request.VerifyUserClientReqDto;
import com.example.itshop.dto.client.response.LoginClientResDto;
import com.example.itshop.dto.client.response.UserClientResDto;
import com.example.itshop.entities.User;
import com.example.itshop.entities.UserVerification;
import com.example.itshop.enums.ClientType;
import com.example.itshop.enums.UserStatus;
import com.example.itshop.properties.SecurityProperty;
import com.example.itshop.repositories.UserRecoveryRepository;
import com.example.itshop.repositories.UserVerificationRepository;
import com.example.itshop.repositories.UserRepository;
import com.example.itshop.utils.EmailUtil;
import com.example.itshop.utils.SecurityUtil;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserClientService {
    private final UserRepository usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final SecurityProperty securityProperty;
    private final UserVerificationRepository userVerificationRepo;
    private final UserRecoveryRepository userRecoveryRepo;
    private final EmailUtil emailUtil;


    public UserClientResDto getCurrentUser() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       User user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new UserClientResDto(user);
    }

    public UserClientResDto create(CreateUserClientReqDto dto) {
        boolean isExisted = usersRepo.existsByEmail(dto.getEmail());
        if(isExisted) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already existed");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        String secret = "verification_" + UUID.randomUUID();
        String hashedSecret = passwordEncoder.encode(secret);
        UserVerification userVerification = new UserVerification();
        userVerification.setSecret(hashedSecret);
        userVerification.setExpiresAt(
                OffsetDateTime.now().plus(securityProperty.getVerificationSecretExpiresIn(), ChronoUnit.SECONDS) );

        user.setUserVerification(userVerification);
        userVerification.setUser(user);
        usersRepo.save(user);
    
        emailUtil.sendVerificationEmail(new AuthEmailDto(dto.getEmail(), 
            this.generateVerificationLink(user.getId(), secret)));

        return new UserClientResDto(user);
    }

    public UserClientResDto update(UpdateUserClientReqDto dto) {
        User user = SecurityUtil.getCurrentUser();

        if(user.getStatus() != UserStatus.ACTIVE)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "User not active");

        if(Strings.hasText(dto.getEmail())) {
           if(usersRepo.existsByEmail(dto.getEmail()))
               throw new ResponseStatusException(HttpStatus.CONFLICT, "Email in use");
           user.setEmail(user.getEmail());
           user.setStatus(UserStatus.PENDING_VERIFICATION);
        }

        if(Strings.hasText(dto.getPassword())) {
          user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if(Strings.hasText(dto.getName())) {
            user.setName(dto.getName());
        }

        usersRepo.save(user);
        return new UserClientResDto(user);
    }

    public LoginClientResDto verify(VerifyUserClientReqDto dto) {
        User user = usersRepo.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        UserVerification userVerification = user.getUserVerification();

        if(Objects.isNull(userVerification)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "User verification not found");
        }

        if( OffsetDateTime.now().isAfter(userVerification.getExpiresAt()))  {
             userVerificationRepo.delete(userVerification);
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expiration");
        }

        if(!passwordEncoder.matches(dto.getSecret(), userVerification.getSecret()))   {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Secret is incorrect");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setUserVerification(null);
        usersRepo.save(user);

        String accessToken = securityUtil.createAccessToken(user.getId(), ClientType.USER);

        return new LoginClientResDto(user, accessToken, null);
    }

    public LoginClientResDto login(UserLoginClientReqDto dto) {
        User user = usersRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong credentials");
        }

        String accessToken = securityUtil.createAccessToken(user.getId(), ClientType.USER);

        return new LoginClientResDto(user, accessToken, null);
    }
    
    private String generateVerificationLink(Long userId, String secret) {
        return "http://localhost/user/verify/" + userId + "/" + secret;
    }
}
