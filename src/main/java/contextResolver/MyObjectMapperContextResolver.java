package contextResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MyObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper;

    public MyObjectMapperContextResolver() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        // Add your custom configurations here...
    }


    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

}