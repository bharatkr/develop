/*

 */

package org.gmu.chess.components;

import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.models.UserDetailsImpl;
import org.gmu.chess.models.UserInformation;
import org.gmu.chess.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        logger.info("Fetching the user -> {} ", username);

        UserInformation userInformation;
        try {
            userInformation = userRepository.getUserByName(username);
        } catch (UserException userException) {
            String message = String.format("Unable to find user %s", username);
            logger.warn(message);

            throw new UsernameNotFoundException(message, userException);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userInformation.getRoleAsString()));

        return new UserDetailsImpl(userInformation.getId(), userInformation.getName(), userInformation.getHash(), userInformation.getEmail(), true, true, true, true, authorities);
    }
}