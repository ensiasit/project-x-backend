package com.ensiasit.projectx.annotations;

import com.ensiasit.projectx.exceptions.ForbiddenException;
import com.ensiasit.projectx.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SecureAdminAspect {
    private final AdminService adminService;

    @Around("@annotation(com.ensiasit.projectx.annotations.SecureAdmin)")
    public Object secureAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        String principalEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!adminService.isAdmin(principalEmail)) {
            throw new ForbiddenException(String.format("Admin access required. User %s is not admin.", principalEmail));
        }

        return joinPoint.proceed();
    }
}