package mzc.test;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;

public class PcfBuildUtils {
	
    public DefaultConnectionContext connectionContext(String apiHost) {
        return DefaultConnectionContext.builder()
            .apiHost(apiHost)
            .skipSslValidation(true)
            .build();
    }
    public PasswordGrantTokenProvider tokenProvider(String username,
                                             String password) {
        return PasswordGrantTokenProvider.builder()
            .password(password)
            .username(username)
            .build();
    }
    public ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
            .connectionContext(connectionContext)
            .tokenProvider(tokenProvider)
            .build();
    }
}
