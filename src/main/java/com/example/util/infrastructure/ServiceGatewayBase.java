package com.example.util.infrastructure;

import com.example.util.infrastructure.externalwebservice.ExternalWebServiceStub;


/**
 * Service gateway base.
 *
 */
public abstract class ServiceGatewayBase {
    /** External service provider. */
    private AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider;

    /**
     * Service gateway base.
     * @param authenticatedExternalServiceProvider to set.
     */
    public ServiceGatewayBase(AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider) {
        this.authenticatedExternalServiceProvider = authenticatedExternalServiceProvider;
    }

    /**
     * External service.
     * @return ExternalWebServiceStub
     */
    protected ExternalWebServiceStub externalService() {
        return authenticatedExternalServiceProvider.provide().getExternalWebService();
    }
}
