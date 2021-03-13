package it.tuccia.event.config;

import it.tuccia.event.annotation.EnableEventEngine;
import it.tuccia.event.annotation.MyEvent;
import it.tuccia.event.engine.Event;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


@CommonsLog
@Order(1)
@Getter
public class EventInitializer implements ImportBeanDefinitionRegistrar {

    AutowireCapableBeanFactory beanFactory;

    public static Map<String, Event> map = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableEventEngine.class.getName()));
        Assert.notNull(attributes, "You must insert @EnableEventEngine");
        Assert.notNull(attributes.get("basePackageScan"), "BasePackageScan must not be null");

        if (attributes.get("basePackageScan").equals(""))
            throw new IllegalArgumentException("BasePackageScan must not be empty");

        this.beanFactory = (AutowireCapableBeanFactory) registry;
        String basePackageScan = (String) attributes.get("basePackageScan");
        map = register(basePackageScan);
    }


    public Map<String, Event> register(String packageName) {

        Map<String, Event> map = new HashMap<>();
        List<Class<?>> listClass = getClassesInPackage(packageName);
        listClass.forEach(clazz -> {
            try {
                if (!clazz.isInterface() && Event.class.isAssignableFrom(clazz)) {
                    Object instance = clazz.newInstance();

                    MyEvent myEventAnnotation = instance.getClass().getAnnotation(MyEvent.class);
                    put(map, myEventAnnotation, (Event) instance);
                    beanFactory.autowireBean(instance);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        map.forEach((key, value) -> log.info("Event ".concat(key).concat(" loaded.")));

        return map;
    }


    private void put(Map<String, Event> map, MyEvent myEventAnnotation, Event eventClazz) {
        String key = convertToCamelCaseWithUnderscore(eventClazz.getClass().getSimpleName());
        if (myEventAnnotation != null && !myEventAnnotation.value().equals(""))
            key = myEventAnnotation.value();

        map.put(key, eventClazz);
    }

    private String convertToCamelCaseWithUnderscore(String s) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return s.replaceAll(regex, replacement).toUpperCase();
    }


    private List<Class<?>> getClassesInPackage(String packageName) {


        String path = packageName.replace(".", File.separator);
        List<Class<?>> classes = new ArrayList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));

        String name;
        for (String classpathEntry : classPathEntries) {
            if (classpathEntry.endsWith(".jar")) {
                File jar = new File(classpathEntry);
                try {
                    JarInputStream is = new JarInputStream(new FileInputStream(jar));
                    JarEntry entry;
                    while ((entry = is.getNextJarEntry()) != null) {
                        name = entry.getName();
                        if (name.endsWith(".class")) {
                            if (name.contains(path) && name.endsWith(".class")) {
                                String classPath = name.substring(0, entry.getName().length() - 6);
                                classPath = classPath.replaceAll("[\\|/]", ".");
                                classes.add(Class.forName(classPath));
                            }
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            } else {
                try {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : Objects.requireNonNull(base.listFiles())) {
                        name = file.getName();
                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.length() - 6);
                            classes.add(Class.forName(packageName + "." + name));
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            }
        }

        return classes;
    }


}
