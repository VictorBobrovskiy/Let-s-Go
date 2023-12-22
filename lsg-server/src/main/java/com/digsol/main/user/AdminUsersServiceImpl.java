package com.digsol.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.error.NotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminUsersServiceImpl implements AdminUsersService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getUsers(Collection<Long> ids, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        Collection<User> users = userRepository.getUsers(ids, pageable).toList();

        log.debug("----- {} users found", users.size());

        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
    }

    @Override
    public UserDto addUser(UserRequestDTO userRequestDTO) {

        User user = userRepository.save(UserMapper.toUser(userRequestDTO));

        log.debug("----- User {} saved", user.getId());

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.debug("----- User {} deleted", userId);
    }
}