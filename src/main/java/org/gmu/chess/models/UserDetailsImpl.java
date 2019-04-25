/*

 */

package org.gmu.chess.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Created by Yannick on 25/9/2015.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -124649164513355612L;
    private int userId;
    private String usr;
    private String pwd;
    private String email;
    private List<UUID> games;

    private boolean isExpired;
    private boolean isLocked;
    private boolean isPwdExpired;
    private boolean isEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(int userId, String usr, String pwd, String email, boolean isExpired, boolean isLocked, boolean isPwdExpired, boolean isEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.usr = usr;
        this.pwd = pwd;
        this.email = email;
        this.isExpired = isExpired;
        this.isLocked = isLocked;
        this.isPwdExpired = isPwdExpired;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
        games = new ArrayList<>();
    }

    public void addGame(UUID game) {
        if (Objects.nonNull(game)) {
            games.add(game);
        }
    }

    public boolean isInGame(UUID game) {
        return games.contains(game);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return pwd;
    }

    public String getUsername() {
        return usr;
    }

    public boolean isAccountNonExpired() {
        return isExpired;
    }

    public boolean isAccountNonLocked() {
        return isLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isPwdExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
