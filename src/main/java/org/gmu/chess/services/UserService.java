/*

 */

package org.gmu.chess.services;

import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.models.User;
import org.gmu.chess.models.UserDetailsImpl;
import org.gmu.chess.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Validated
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewUser(@NotNull User user) throws UserException {
        userRepository.addNewUser(user);
    }

    public void updateUser(@Valid @NotNull User user, @NotNull UserDetailsImpl principal) throws UserException {
        userRepository.updateUser(user, principal);
    }

    public void addGameToUser(@NotEmpty String username, @NotNull UUID game) throws UserException {
        userRepository.addGameToUser(username, game);
    }
}
