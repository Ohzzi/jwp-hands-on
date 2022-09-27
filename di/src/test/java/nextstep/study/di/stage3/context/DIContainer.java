package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        try {
            injectDependencies();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::instantiate)
                .collect(Collectors.toSet());
    }

    private Object instantiate(final Class<?> aClass) {
        try {
            Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectDependencies() throws IllegalAccessException {
        for (Object bean : this.beans) {
            setFields(bean);
        }
    }

    private void setFields(final Object bean) throws IllegalAccessException {
        Field[] declaredFields = bean.getClass()
                .getDeclaredFields();
        for (Field field : declaredFields) {
            setField(bean, field);
        }
    }

    private void setField(final Object instance, final Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        if (field.get(instance) != null) {
            return;
        }
        field.set(instance, getRequiredBean(fieldType));
    }

    private Object getRequiredBean(final Class<?> aClass) {
        return beans.stream()
                .filter(bean -> isRequiredType(bean, aClass))
                .findAny()
                .orElseThrow(() -> new RuntimeException("의존성 주입에 필요한 빈이 존재하지 않습니다."));
    }
    
    private boolean isRequiredType(final Object bean, final Class<?> requiredClass) {
        Class<?> aClass = bean.getClass();
        if (aClass == requiredClass) {
            return true;
        }
        for (Class<?> aInterface : aClass.getInterfaces()) {
            if (aInterface == requiredClass) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(bean -> isRequiredType(bean, aClass))
                .map(bean -> (T) bean)
                .findAny()
                .orElseThrow(() -> new RuntimeException("타입에 해당하는 빈이 존재하지 않습니다."));
    }
}
