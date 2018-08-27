package com.whuthm.happychat.internal.context;

public class AbstractApplicationService {

    private final ApplicationServiceContext applicationServiceContext;

    protected AbstractApplicationService(ApplicationServiceContext applicationServiceContext) {
        this.applicationServiceContext = applicationServiceContext;
    }

    public ApplicationServiceContext getApplicationServiceContext() {
        return applicationServiceContext;
    }
}
