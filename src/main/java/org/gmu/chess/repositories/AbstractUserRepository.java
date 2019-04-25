/*

 */

package org.gmu.chess.repositories;

import org.apache.commons.lang3.ArrayUtils;
import org.gmu.chess.exceptions.UserAlreadyExistException;
import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.models.Roles;
import org.gmu.chess.models.User;
import org.gmu.chess.models.UserDetailsImpl;
import org.gmu.chess.models.UserInformation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public abstract class AbstractUserRepository implements UserRepository {
    private final PasswordEncoder passwordEncoder;

    public AbstractUserRepository(@NotNull PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public final void addNewUserWithRole(@Valid @NotNull User user, @NotNull Roles role) throws UserException {
        saveOrUpdateUserIfNotExist(user, role);
    }

    @Override
    public void addNewUser(@Valid @NotNull User user) throws UserException {
        saveOrUpdateUserIfNotExist(user);
    }

    /**
     * TODO: Implements the email change feature
     *
     * @param user
     * @param userDetails
     */
    @Override
    public void updateUser(@Valid @NotNull User user, @NotNull UserDetailsImpl userDetails) {
        throw new UnsupportedOperationException();
    }

    private void saveOrUpdateUserIfNotExist(@Valid @NotNull User user, Roles... role) throws UserException {
        if (!isUserExist(user.getName())) {
            UserInformation userInformation;

            if (ArrayUtils.isEmpty(role)) {
                userInformation = createUserCredentialsFromUser(user);
            } else {
                //TODO: Support multiples roles
                userInformation = createUserCredentialsFromUser(user).withRole(role[0]);
            }

            saveOrUpdateUserInformation(userInformation);
        } else {
            throw new UserAlreadyExistException();
        }
    }

    protected boolean isUserExist(@NotNull String username) {
        try {
            getUserByName(username);
            return true;
        } catch (UserException e) {
            return false;
        }
    }

    private UserInformation createUserCredentialsFromUser(@Valid @NotNull User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        return new UserInformation(user.getName(), encodedPassword, user.getEmail());
    }

    protected abstract void saveOrUpdateUserInformation(@NotNull UserInformation userInformation);
}
