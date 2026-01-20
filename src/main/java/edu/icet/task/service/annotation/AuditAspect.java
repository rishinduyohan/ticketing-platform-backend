package edu.icet.task.service.annotation;

import edu.icet.task.model.entity.AuditLog;
import edu.icet.task.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @AfterThrowing(pointcut = "@annotation(AuditFailure)", throwing = "ex")
    public void logFailure(JoinPoint joinPoint, Exception ex) {
        AuditLog log = new AuditLog();

        log.setAction("BOOKING_FAILED");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(ex.getMessage());

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Long) {
            log.setUserId((Long) args[0]);
        }

        auditLogRepository.save(log);
    }
}