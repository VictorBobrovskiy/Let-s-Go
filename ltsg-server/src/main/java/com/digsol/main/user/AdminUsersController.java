package com.digsol.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(
            @RequestParam(required = false) Collection<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("----- Users requested by admin");
        return ResponseEntity.ok(adminUsersService.getUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.debug("----- Add new user requested by admin");
        return new ResponseEntity<>(adminUsersService.addUser(userRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        log.debug("----- Delete user requested by admin");
        adminUsersService.deleteUser(userId);
    }
}