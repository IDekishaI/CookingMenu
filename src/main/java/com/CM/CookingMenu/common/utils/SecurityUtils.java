package com.CM.CookingMenu.common.utils;

import com.CM.CookingMenu.auth.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    public static boolean isCookOrAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(
                authority ->
                        authority.getAuthority().equals("ROLE_COOK") ||
                        authority.getAuthority().equals("ROLE_ADMIN")
        );
    }
}
