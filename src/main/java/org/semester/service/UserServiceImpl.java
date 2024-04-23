package org.semester.service;

import lombok.AllArgsConstructor;
import org.semester.dto.EventDto;
import org.semester.dto.RoleDto;
import org.semester.dto.UserDto;
import org.semester.entity.Event;
import org.semester.entity.EventImage;
import org.semester.entity.User;
import org.semester.mappers.EventMapper;
import org.semester.mappers.RoleMapper;
import org.semester.mappers.UserMapper;
import org.semester.repository.EventRepository;
import org.semester.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private UserMapper userMapper;
    private EventMapper eventMapper;
    private RoleMapper roleMapper;
    private static final int PAGE_SIZE = 10;
    private Environment environment;
    private static final String envPath = "spring.servlet.multipart.location";

    @Override
    public User addUser(User user) {
        user.setPassword((new BCryptPasswordEncoder()).encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
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
            UserDto userDto = findByEmail(userEmail);
            userRepository.saveAndFlush(userMapper.getUserEntity(userDto));
            file.transferTo(new File(environment.getProperty(envPath) + name));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Boolean deleteProfileImage(String userEmail) {
        UserDto userDto = findByEmail(userEmail);
        if (userDto == null) {
            return false;
        }
        try {
            Files.delete(Path.of(environment.getProperty(envPath) + userDto.getUserImage()));
        } catch (IOException e) {
            return false;
        }
        userRepository.saveAndFlush(userMapper.getUserEntity(userDto));
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
        UserDto userDto = findByEmail(email);
        if (userDto == null) {
            throw new UsernameNotFoundException(String.format("Email '%s' was not found", email));
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userDto.getName())
                .authorities(new SimpleGrantedAuthority(userDto.getRole().getRole()))
                .build();
    }

    @Override
    public RoleDto getRole(String email) {
        return roleMapper.getRoleDto(userRepository.findByEmail(email).getRole());
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
