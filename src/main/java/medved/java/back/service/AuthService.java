package medved.java.back.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medved.java.back.dto.AuthResponseDto;
import medved.java.back.dto.LoginDto;
import medved.java.back.entity.Role;
import medved.java.back.entity.UserEntity;
import medved.java.back.jwt.JwtGenerator;
import medved.java.back.repository.RoleRepository;
import medved.java.back.repository.TokenRepository;
import medved.java.back.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private TokenRepository tokenRepository;

    public AuthResponseDto login(LoginDto loginDto) {
        log.info("-> Login user: {}", loginDto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        tokenRepository.saveToken(token, loginDto.getLogin());
        return new AuthResponseDto(token);
    }

    public ResponseEntity<String> register(LoginDto loginDto) {
        if (userRepository.existsByUsername(loginDto.getLogin())) {
            return new ResponseEntity<>("Username is busy!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(loginDto.getLogin());
        user.setPassword(passwordEncoder.encode(loginDto.getPassword()));

        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
        return new ResponseEntity<String>("User added to Database!", HttpStatus.OK);
    }
}
