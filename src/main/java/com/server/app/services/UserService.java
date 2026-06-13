package com.server.app.services;

import com.server.app.config.JsonWebToken;
import com.server.app.dto.auth.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.app.dto.user.UserCreateDto;
import com.server.app.dto.user.UserUpdateDto;
import com.server.app.entities.Role;
import com.server.app.entities.User;
import com.server.app.exceptions.ConfictException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.RoleRepository;
import com.server.app.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JsonWebToken jsonWebToken;


    @Transactional
    public User create(UserCreateDto dto) {
        uniqueUsername(dto.getUsername(), null);
        uniqueEmail(dto.getEmail(), null);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getRole() != null) {
            Role role = roleRepository.findById(dto.getRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    public Page<User> findAll(int page, int size, String search) {
        return userRepository.findAll(PageRequest.of(page, size), search);
    }

    @Transactional
    public User updateUser(int userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new NotFoundException("The user wasnt found");
        }

        if (user.isBlocked()) {
            throw new ConfictException("The user: " + user.getUsername() + " is locked");
        }

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            uniqueUsername(dto.getUsername(), userId);
            user.setUsername(dto.getUsername());
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
            user.setSurname(dto.getSurname());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            uniqueEmail(dto.getEmail(), userId);
            user.setEmail(dto.getEmail());
        }

        if (dto.getBlocked() != null) {
            user.setBlocked(dto.getBlocked());
        }

        if (dto.getRole() != null) {
            Role role = roleRepository.findById(dto.getRole())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    private void uniqueUsername(String username, Integer id) {
        userRepository.findUserByUsername(username).ifPresent(existing -> {
            if (id == null || existing.getId() != id) {
                throw new ConfictException("El nombre de usuario ya está en uso");
            }
        });
    }

    private void uniqueEmail(String email, Integer id) {
        userRepository.findUserByEmail(email).ifPresent(existing -> {
            if (id == null || existing.getId() != id) {
                throw new ConfictException("El correo electrónico ya está en uso");
            }
        });
    }

    public User findById(int Id){
        User user = userRepository.findById(Id).orElse(null);

        if (user == null){
            throw new NotFoundException("User not found");
        }
        return user;

    }

    @Transactional
    public loginResponse login(
            loginDTO request
    ) {

        User user = userRepository
                .findUserByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            throw new NotFoundException("User wasn't found");
        }

        if (user.isBlocked()) {
            throw new ConfictException(
                    "The user: " + user.getUsername() + " is locked"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new ConfictException("Invalid credentials");
        }

        String token = jsonWebToken.createToken(user);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole()
        );

        return new loginResponse(token, response);
    }


    @Transactional
    public loginResponse signup(
            SignupRequest dto
    ) {

        uniqueUsername(dto.getUsername(), null);
        uniqueEmail(dto.getEmail(), null);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        Role role = roleRepository.findById((1L))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRole(role);


        User signup = userRepository.save(user);

        System.out.println("Usuario guardado ID = " + signup.getId());

        String token = jsonWebToken.createToken(signup);

        System.out.println("Token generado = " + token);
        UserResponse response = new UserResponse(signup.getId(),signup.getUsername(),signup.getName(),signup.getSurname(),signup.getEmail(),signup.getRole());


        return new loginResponse(token, response);

    }

    public loginResponse profile(String token){
        Integer id = jsonWebToken.extractIdUser(token);
        User user = userRepository.findById(id).orElse(null);

        if(user == null){
            throw new NotFoundException("No encontrado el usuario");
        }

        UserResponse response = new UserResponse(user.getId(),user.getUsername(),user.getName(),user.getSurname(),user.getEmail(),user.getRole());
        return new loginResponse(token, response);
    }


    @Transactional
    public loginResponse updateProfile(
            String token,
            UpdateProfileRequest request
    ) {

        Integer id = jsonWebToken.extractIdUser(token);

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException("No encontrado el usuario");
        }

        if (request.getUsername() != null &&
                !request.getUsername().isBlank()) {

            uniqueUsername(request.getUsername(), user.getId());

            user.setUsername(request.getUsername());
        }

        if (request.getName() != null &&
                !request.getName().isBlank()) {

            user.setName(request.getName());
        }

        if (request.getSurname() != null &&
                !request.getSurname().isBlank()) {

            user.setSurname(request.getSurname());
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank()) {

            uniqueEmail(request.getEmail(), user.getId());

            user.setEmail(request.getEmail());
        }

        User updated = userRepository.save(user);

        String newToken = jsonWebToken.createToken(updated);

        UserResponse response = new UserResponse(
                updated.getId(),
                updated.getUsername(),
                updated.getName(),
                updated.getSurname(),
                updated.getEmail(),
                updated.getRole()
        );

        return new loginResponse(newToken, response);
    }


    @Transactional
    public loginResponse updatePassword(
            String token,
            UpdatePasswordRequest request
    ) {

        Integer id = jsonWebToken.extractIdUser(token);

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException("No encontrado el usuario");
        }

        if (!passwordEncoder.matches(
                request.getOldpassword(),
                user.getPassword()
        )) {
            throw new ConfictException("La contraseña actual es incorrecta");
        }

        if (!request.getNewpassword()
                .equals(request.getConfirmpassword())) {

            throw new ConfictException(
                    "La nueva contraseña y la confirmación no coinciden"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewpassword()
                )
        );

        User updated = userRepository.save(user);

        String newToken = jsonWebToken.createToken(updated);

        UserResponse response = new UserResponse(
                updated.getId(),
                updated.getUsername(),
                updated.getName(),
                updated.getSurname(),
                updated.getEmail(),
                updated.getRole()
        );

        return new loginResponse(newToken, response);
    }



}
