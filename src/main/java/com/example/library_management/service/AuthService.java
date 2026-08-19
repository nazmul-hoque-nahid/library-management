package com.example.library_management.service;

import com.example.library_management.dto.AuthResponse;
import com.example.library_management.dto.ChangePasswordRequest;
import com.example.library_management.dto.LoginRequest;
import com.example.library_management.dto.RegisterRequest;
import com.example.library_management.entity.User;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.repository.UserRepository;
import com.example.library_management.security.CustomUserDetails;
import com.example.library_management.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

@Transactional
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already Exist");
        }
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.MEMBER);
        userRepository.save(user);
        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        String token=jwtService.generateToken(customUserDetails);
        AuthResponse response=new AuthResponse();
        response.setToken(token);
        return response;
    }

    public AuthResponse login(LoginRequest request){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authentication =  authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        AuthResponse response=new AuthResponse();
        response.setToken(token);
        return response;
    }

    public void changePassword(ChangePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Authentication>optionalAuthentication=Optional.ofNullable(authentication);
        String email = optionalAuthentication.map(Authentication::getName).orElse(null);
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(),user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
