package org.semester.service;

import lombok.AllArgsConstructor;
import org.semester.dto.*;
import org.semester.entity.*;
import org.semester.mappers.EventMapper;
import org.semester.mappers.RoleMapper;
import org.semester.mappers.UserMapper;
import org.semester.repository.EventRepository;
import org.semester.repository.UserRepository;
import org.semester.util.TokenUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private UserMapper userMapper;
    private EventMapper eventMapper;
    private RoleMapper roleMapper;
    private TokenUtil tokenUtil;
    private TokenService tokenService;
    private static final int PAGE_SIZE = 10;
    private Environment environment;
    private static final String envPath = "spring.servlet.multipart.location";

    @Override
    public Map<String, String> addUser(RegisterUserDto registerUserDto) {
        User newUser = User.builder().build();
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword((new BCryptPasswordEncoder()).encode(registerUserDto.getPassword()));
        newUser.setUserImage("default.jpg");
        Role role = Role.builder()
                .id(1L)
                .role("ROLE_USER")
                .build();
        newUser.setRole(role);
        newUser.setAge(20);
        newUser.setCity("");
        newUser.setIsBanned(false);
        userRepository.saveAndFlush(newUser);
        Map<String, String> tokens = tokenUtil.generatePair(newUser.getEmail(), role.getRole());
        tokenService.addToken(Token.builder()
                .token(tokens.get("refresh"))
                .isRevoked(false)
                .build());
        return tokens;
    }

    @Override
    public List<UserDto> getUsers(Integer page) {
        return userRepository.findAll(PageRequest.of(page, PAGE_SIZE)).map(userMapper::getUserDto).toList();
    }

    @Override
    public List<UserDto> findByNameContaining(String name, Integer page) {
        return userRepository.findByNameContaining(name, PageRequest.of(page, PAGE_SIZE))
                .stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user == null ? null : userMapper.getUserDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        return userMapper.getUserDto(user);
    }

    @Override
    public List<UserDto> getSusbcribers(Long id, Integer page) {
        return userRepository.findSubscribers(id, PageRequest.of(page, PAGE_SIZE)).stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getSubscribees(Long id, Integer page) {
        return userRepository.findSubscribees(id, PageRequest.of(page, PAGE_SIZE))
                .stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            return;
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(Long id) {
        UserDto userDto = findById(id);
        try {
            Files.delete(Path.of(environment.getProperty(envPath) + userDto.getUserImage()));
            userRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getProfileImage(Long id) {
        String name = findById(id).getUserImage();
        try {
            return Files.readAllBytes(Path.of(environment.getProperty(envPath) + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getProfileImageByFileName(String fileName) {
        try {
            return Files.readAllBytes(Path.of(environment.getProperty(envPath) + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean addProfileImage(MultipartFile file, String userEmail) {
        if (!file.getContentType().equals("image/png")
                && !file.getContentType().equals("image/jpg")
                && !file.getContentType().equals("image/jpeg")
        ) {
            return false;
        }
        try {
            String type = (file.getContentType().equals("image/png")) ? "png" : "jpg";
            String name = UUID.randomUUID() + "." + type;
            User user = userRepository.findByEmail(userEmail);
            user.setUserImage(name);
            userRepository.saveAndFlush(user);
            file.transferTo(new File(environment.getProperty(envPath) + name));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Boolean deleteProfileImage(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return false;
        }
        try {
            Files.delete(Path.of(environment.getProperty(envPath) + user.getUserImage()));
        } catch (IOException e) {
            return false;
        }
        user.setUserImage(null);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public Boolean subscribeToEvent(String email, Long eventId) {
        User user = userRepository.findByEmail(email);
        if (user.getSubscribedEvents().stream().anyMatch(event -> Objects.equals(event.getId(), eventId))) {
            return false;
        }
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return false;
        }
        user.getSubscribedEvents().add(event.get());
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public Boolean unsubscribeFromEvent(String email, Long eventId) {
        User user = userRepository.findByEmail(email);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return false;
        }
        user.getSubscribedEvents().remove(event.get());
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public List<EventDto> getSubscribedEvents(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> user.getSubscribedEvents().stream()
                .map(eventMapper::getEventDto)
                .toList()).orElse(null);
    }

    @Override
    public String getPassword(String email) {
        return userRepository.findByEmail(email).getPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Email '%s' was not found", email));
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .authorities(new SimpleGrantedAuthority(user.getRole().getRole()))
                .build();
    }

    @Override
    public RoleDto getRole(String email) {
        return roleMapper.getRoleDto(userRepository.findByEmail(email).getRole());
    }

    @Override
    public FullUserDto getFullUserByEmail(String email) {
        User found = userRepository.findByEmail(email);
        if (found == null) {
            return null;
        }
        return userMapper.getFullUserDto(found);
    }

    @Override
    public Boolean changeIsBanned(Long id) {
        Optional<User> found = userRepository.findById(id);
        if (found.isPresent()) {
            User u = found.get();
            u.setIsBanned(!u.getIsBanned());
            userRepository.saveAndFlush(u);
            return true;
        }
        return false;
    }
}
