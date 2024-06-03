package org.semester.service;

import lombok.AllArgsConstructor;
import org.semester.dto.*;
import org.semester.dto.eventDto.EventDto;
import org.semester.dto.userDto.FullUserDto;
import org.semester.dto.userDto.RegisterUserDto;
import org.semester.dto.userDto.UserDto;
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

import static org.semester.util.StaticString.*;


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
        User newUser = User.builder()
                .name(registerUserDto.getName())
                .email(registerUserDto.getEmail())
                .password((new BCryptPasswordEncoder()).encode(registerUserDto.getPassword()))
                .userImage("default.png")
                .age(20)
                .city("")
                .isBanned(false)
                .build();
        Role role = Role.builder()
                .id(1L)
                .role("ROLE_USER")
                .build();
        newUser.setRole(role);
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
        if (user == null) {
            return null;
        }
        return userMapper.getUserDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
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
        return userRepository.findAuthors(id, PageRequest.of(page, PAGE_SIZE))
                .stream()
                .map(userMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean subscribe(Long idAuthor, Long idSubscriber) {
        User author = userRepository.findById(idAuthor).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        User subscriber = userRepository.findById(idSubscriber).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        if (author.getSubscribers().contains(subscriber)) {
            author.getSubscribers().remove(subscriber);
        } else {
            author.getSubscribers().add(subscriber);
        }
        userRepository.saveAndFlush(author);
        return true;
    }

    @Override
    public Boolean amISubscribedToUser(Long idAuthor, Long idSubscriber) {
        User author = userRepository.findById(idAuthor).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        User subscriber = userRepository.findById(idSubscriber).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        return author.getSubscribers().contains(subscriber);
    }

    @Override
    public Boolean updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setCity(userDto.getCity());
        userRepository.saveAndFlush(user);
        return true;
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
            throw new RuntimeException(ERROR_ON_FILE_READ.getValue());
        }
    }

    @Override
    public byte[] getProfileImageByFileName(String fileName) {
        try {
            File file = new File(environment.getProperty(envPath) + fileName);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(ERROR_ON_FILE_READ.getValue());
        }
    }

    @Override
    public String addProfileImage(MultipartFile file, String userEmail) {
        if (!file.getContentType().equals("image/png")
                && !file.getContentType().equals("image/jpg")
                && !file.getContentType().equals("image/jpeg")
        ) {
            throw new RuntimeException(WRONG_FILE_TYPE.getValue());
        }
        try {
            String type = (file.getContentType().equals("image/png")) ? "png" : "jpg";
            String name = UUID.randomUUID() + "." + type;
            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                throw new NoSuchElementException(USER_NOT_FOUND.getValue());
            }
            if (!user.getUserImage().equals("default.png")) {
                deleteProfileImage(user.getEmail());
            }
            user.setUserImage(name);
            userRepository.saveAndFlush(user);
            file.transferTo(new File(environment.getProperty(envPath) + name));
            return name;
        } catch (IOException e) {
            throw new RuntimeException(ERROR_ON_FILE_ADD.getValue());
        }
    }

    @Override
    public Boolean deleteProfileImage(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        try {
            Files.delete(Path.of(environment.getProperty(envPath) + user.getUserImage()));
        } catch (IOException e) {
            throw new RuntimeException(ERROR_ON_FILE_DELETE.getValue());
        }
        user.setUserImage("default.png");
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public Boolean manageSubscriptionToEvent(String email, Long eventId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        if (user.getSubscribedEvents().stream().anyMatch(event -> Objects.equals(event.getId(), eventId))) {
            return unsubscribeFromEvent(email, eventId);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND.getValue()));
        user.getSubscribedEvents().add(event);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public Boolean unsubscribeFromEvent(String email, Long eventId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND.getValue()));
        user.getSubscribedEvents().remove(event);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public List<EventDto> getSubscribedEvents(Long id, Integer page) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        int startIndex = PAGE_SIZE * page;
        int endIndex = PAGE_SIZE * (page + 1);
        if (endIndex > user.getSubscribedEvents().size()) {
            if (startIndex > user.getSubscribedEvents().size()) {
                return List.of();
            }
            int currSize = user.getSubscribedEvents().size();
            if (currSize == 0) {
                return List.of();
            }
            return user.getSubscribedEvents().subList(startIndex, currSize).stream()
                    .map(eventMapper::getEventDto)
                    .toList();
        }
        return user.getSubscribedEvents().subList(startIndex, endIndex).stream()
                .map(eventMapper::getEventDto)
                .toList();
    }

    @Override
    public Boolean amISubscribedToEvent(String email, Long eventId) {
        return userRepository.amISubscribedToEvent(userRepository.findByEmail(email).getId(), eventId);
    }

    @Override
    public List<EventDto> getCreatedEvents(Long id, Integer page) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        int startIndex = PAGE_SIZE * page;
        int endIndex = PAGE_SIZE * (page + 1);
        if (endIndex > user.getEventList().size()) {
            if (startIndex > user.getEventList().size()) {
                return List.of();
            }
            int currSize = user.getEventList().size();
            if (currSize == 0) {
                return List.of();
            }
            return user.getEventList().subList(startIndex, currSize).stream()
                    .map(eventMapper::getEventDto)
                    .toList();
        }
        return user.getEventList().subList(startIndex, endIndex).stream()
                .map(eventMapper::getEventDto)
                .toList();
    }

    @Override
    public String getPassword(String email) {
        return userRepository.findByEmail(email).getPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .authorities(new SimpleGrantedAuthority(user.getRole().getRole()))
                .build();
    }

    @Override
    public RoleDto getRole(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        return roleMapper.getRoleDto(user.getRole());
    }

    @Override
    public FullUserDto getFullUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }
        return userMapper.getFullUserDto(user);
    }

    @Override
    public List<UserDto> getRecommendedUsers(Long id, Integer page) {
        return userRepository.getUserRecommendations(id, page).stream().map(userMapper::getUserDto).collect(Collectors.toList());
    }

    @Override
    public Boolean changeIsBanned(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getValue()));
        user.setIsBanned(!user.getIsBanned());
        userRepository.saveAndFlush(user);
        return true;
    }
}
