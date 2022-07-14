package co.com.bancolombia.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RouterTest {

    @InjectMocks
    private Handler handler;

    @Test
    void routerTest() {
        MockitoAnnotations.openMocks(this);
        Router router = new Router(new ApiProperties(
                "/api/v1",
                "/template/create",
                "/template/get",
                "/template/update",
                "/template/delete",
                "/template/createmessage"
        ));
        router.routerFunction(handler);
        assertThat(router).isInstanceOf(Router.class);
    }
}
