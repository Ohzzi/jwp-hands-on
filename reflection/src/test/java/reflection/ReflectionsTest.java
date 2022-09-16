package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(type -> log.info(type.getSimpleName()));
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        services.forEach(type -> log.info(type.getSimpleName()));
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        repositories.forEach(type -> log.info(type.getSimpleName()));
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
