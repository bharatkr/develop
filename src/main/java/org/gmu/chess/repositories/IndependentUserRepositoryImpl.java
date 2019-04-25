/*

 */

package org.gmu.chess.repositories;

import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.exceptions.UserNotFoundException;
import org.gmu.chess.models.UserInformation;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IndependentUserRepositoryImpl extends AbstractUserRepository {
    protected final Map<String, UserInformation> users = new HashMap<>();

    public IndependentUserRepositoryImpl(PasswordEncoder passwordEncoder) {
        super(passwordEncoder);
    }

    @Override
    protected void saveOrUpdateUserInformation(@NotNull UserInformation userInformation) {
        users.put(userInformation.getName(), userInformation);
    }

    @Override
    public UserInformation getUserByName(@NotBlank String username) throws UserException {
        return Optional.ofNullable(users.get(username)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserInformation> getUserByEmail(@NotBlank @Email String email) throws UserException {
        Predicate<UserInformation> userCredentialsPredicate = userCredentials -> email.equals(userCredentials.getEmail());

        List<UserInformation> values = users.values()
                .parallelStream()
                .filter(userCredentialsPredicate)
                .collect(Collectors.toList());

        if (values.isEmpty()) {
            throw new UserNotFoundException();
        }

        return values;
    }

    @Override
    public void addGameToUser(@NotBlank String username, @NotNull UUID game) throws UserException {
        UserInformation userByName = getUserByName(username);
        userByName.addGame(game);

        saveOrUpdateUserInformation(userByName);
    }
}
