package com.dharaneesh.job_portal_backend.audit;

import com.dharaneesh.job_portal_backend.utils.ApplicationUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component(value = "auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(ApplicationUtils.getLoggedInUser());
    }
}
