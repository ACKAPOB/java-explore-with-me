package ewmservice.client;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class Mapper {
    @Bean
    public ModelMapper getMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(true);
        return mapper;
    }

    public static <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
        return list.stream().map(converter::apply).collect(Collectors.toList());
    }
}
