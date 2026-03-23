package org.example.auth.services;

import org.example.auth.entity.Role;
import org.example.auth.entity.User;
import org.example.auth.entity.UserRegisterDTO;
import org.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private User saveUser(User user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return  userRepository.saveAndFlush(user);
        }

        public String generateToken(String username){
            return jwtService.generateToken(username);
        }

        public void validateToken(String token){
            jwtService.validateToken(token);
        }


        public void register(UserRegisterDTO userRegisterDTO) {
            User user = new User();
            user.setLogin(userRegisterDTO.getLogin());
            user.setPassword(userRegisterDTO.getPassword());
            user.setEmail(userRegisterDTO.getEmail());
            if (userRegisterDTO.getRole() != null){
                user.setRole(userRegisterDTO.getRole());
            }else{
                user.setRole(Role.USER);
            }
        }
    }