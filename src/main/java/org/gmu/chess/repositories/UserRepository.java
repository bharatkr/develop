/*

 */

package org.gmu.chess.repositories;

import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.models.Roles;
import org.gmu.chess.models.User;
import org.gmu.chess.models.UserDetailsImpl;
import org.gmu.chess.models.UserInformation;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface UserRepository {
    void addNewUserWithRole(@Valid @NotNull User user, @NotNull Roles role) throws UserException;

    UserInformation getUserByName(@NotBlank String username) throws UserException;

    List<UserInformation> getUserByEmail(@NotBlank @Email String email) throws UserException;

    void addGameToUser(@NotBlank String username, @NotNull UUID game) throws UserException;

    void addNewUser(@Valid @NotNull User user) throws UserException;

    void updateUser(@Valid @NotNull User user, @NotNull UserDetailsImpl userDetails) throws UserException;
}
