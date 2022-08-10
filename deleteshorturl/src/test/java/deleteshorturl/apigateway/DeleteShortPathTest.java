package deleteshorturl.apigateway;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import deleteshorturl.services.Service;

public class DeleteShortPathTest {
    
    @Mock
    private Service svc;

    @InjectMocks
    private DeleteShortPath deleteShortPath;
}
