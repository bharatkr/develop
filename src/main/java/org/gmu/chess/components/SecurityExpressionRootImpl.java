/*

 */

package org.gmu.chess.components;

import org.gmu.chess.models.UserDetailsImpl;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public class SecurityExpressionRootImpl extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    public SecurityExpressionRootImpl(Authentication authentication) {
        super(authentication);
    }

    public boolean isPlayerInGame(UUID uuid) {
        UserDetailsImpl user = (UserDetailsImpl) this.getPrincipal();
        return user.isInGame(uuid);
    }

    @Override
    public void setFilterObject(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getFilterObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReturnObject(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getReturnObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getThis() {
        throw new UnsupportedOperationException();
    }
}
