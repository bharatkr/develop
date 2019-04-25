/*

 */

package org.gmu.chess.models;

import java.io.Serializable;
import java.util.*;

public class UserInformation implements Serializable {
    private static final long serialVersionUID = 8210548561304905969L;
    private final List<UUID> listOfGames = new ArrayList<>();
    private String name;
    private String hash;
    private String email;
    private Roles role;
    private int id;

    public UserInformation(String name, String hash, String email, Roles role) {
        this.name = name;
        this.hash = hash;
        this.role = role;
        this.email = email;
    }

    public UserInformation(String name, String hash, String email) {
        this.name = name;
        this.hash = hash;
        this.email = email;
        this.role = Roles.USER;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<UUID> getListOfGames() {
        return Collections.unmodifiableList(listOfGames);
    }

    public void addGame(UUID game) {
        listOfGames.add(game);
    }

    public String getRoleAsString() {
        return role.name();
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInformation withRole(Roles role) {
        this.role = role;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformation that = (UserInformation) o;
        return Objects.equals(name, that.name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
