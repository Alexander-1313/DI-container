package com.elinext.container.impl;

import com.elinext.annotation.Inject;
import com.elinext.container.Injector;
import com.elinext.container.Provider;
import com.elinext.context.ApplicationContext;
import com.elinext.exception.BindingNotFoundException;
import com.elinext.exception.ConstructorNotFoundException;
import com.elinext.exception.TooManyConstructorsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

public class InjectorImpl implements Injector {

    private static final Logger LOGGER = LogManager.getLogger(InjectorImpl.class);
    private static Injector INSTANCE;

    ApplicationContext context = ApplicationContext.getInstance();

    public static Injector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InjectorImpl();
        }
        return INSTANCE;
    }

    private InjectorImpl() {
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        List<Class<?>> classes = find("com.elinext.dao.provider");
        Object o = null;

        for (Class<?> clazz : classes) {
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            String substring = null;
            for (Type genType : genericInterfaces) {
                String typeName = genType.getTypeName();
                int i = typeName.indexOf('<');
                substring = typeName.substring(i + 1, typeName.length() - 1);
            }
            if (Provider.class.isAssignableFrom(clazz) && Class.forName(substring) == type) {
                Constructor<?>[] constructors = clazz.getConstructors();
                o = constructors[0].newInstance();
            }
        }

        return (Provider<T>) o;
    }


    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) throws IllegalAccessException, InvocationTargetException, InstantiationException, TooManyConstructorsException, BindingNotFoundException, ConstructorNotFoundException {
        if (intf.isAssignableFrom(impl)) {
            LOGGER.info("Is instance! Binded as prototype");
            Constructor<?>[] constructors = impl.getConstructors();
            Object objectImpl = null;

            int count = 0;
            for(Constructor constructor: constructors){
                Annotation[] annotations = constructor.getDeclaredAnnotations();
                for(Annotation annotation: annotations){
                    if(annotation.annotationType().equals(Inject.class)){
                        count++;
                    }
                }
            }

            if(count > 1){
                throw new TooManyConstructorsException("there are too many constructors with annotation Inject");
            }
            if(count == 0){
                if(constructors.length == 0){
                    throw new ConstructorNotFoundException("There are no such constructor");
                }
                for(Constructor constructor: constructors){
                    if(constructor.getParameters() != null){
                        objectImpl = constructor.newInstance();
                    }
                }
            }

            ArrayList<Object> parameters = new ArrayList<>();
            if(count == 1){
                for(Constructor constructor: constructors){
                    if(constructor.isAnnotationPresent(Inject.class)){
                        Object[] objects = new Object[constructor.getParameters().length];
                        int index = 0;
                        Class[] parameterTypes = constructor.getParameterTypes();
                        for(Class clazz: parameterTypes){
                            Object o = context.getSingletonContainerObjByClass(clazz);
                            if(o == null){
                                o = context.getContainerObjByClass(clazz);
                                if(o == null){
                                    throw new BindingNotFoundException("There are no such binding");
                                }
                            }
                            objects[index++] = o;
                        }
                        objectImpl = constructor.newInstance(objects);
                    }
                }
            }
            context.putObjectIntoContainer(intf, objectImpl);
        } else {
            LOGGER.info(impl.toString() + " is not an implementation of " + intf.toString());
        }

    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) throws BindingNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, TooManyConstructorsException, ConstructorNotFoundException { //TODO
        if (intf.isAssignableFrom(impl)) {
            LOGGER.info("Is instance! Binded as prototype");
            Constructor<?>[] constructors = impl.getConstructors();
            Object objectImpl = null;

            int count = 0;
            for(Constructor constructor: constructors){
                Annotation[] annotations = constructor.getDeclaredAnnotations();
                for(Annotation annotation: annotations){
                    if(annotation.annotationType().equals(Inject.class)){
                        count++;
                    }
                }
            }

            if(count > 1){
                throw new TooManyConstructorsException("there are too many constructors with annotation Inject");
            }
            if(count == 0){
                if(constructors.length == 0){
                    throw new ConstructorNotFoundException("There are no such constructor");
                }
                for(Constructor constructor: constructors){
                    if(constructor.getParameters() != null){
                        objectImpl = constructor.newInstance();
                    }
                }
            }

            ArrayList<Object> parameters = new ArrayList<>();
            if(count == 1){
                for(Constructor constructor: constructors){
                    if(constructor.isAnnotationPresent(Inject.class)){
                        Object[] objects = new Object[constructor.getParameters().length];
                        int index = 0;
                        Class[] parameterTypes = constructor.getParameterTypes();
                        for(Class clazz: parameterTypes){
                            Object o = context.getContainerObjByClass(clazz);
                            if(o == null){
                                o = context.getContainerObjByClass(clazz);
                                if(o == null){
                                    throw new BindingNotFoundException("There are no such binding");
                                }
                            }
                            objects[index++] = o;
                        }
                        objectImpl = constructor.newInstance(objects);
                    }
                }
            }
            context.putSingletonObjectIntoContainer(intf, objectImpl);
        } else {
            LOGGER.info(impl.toString() + " is not an implementation of " + intf.toString());
        }
    }

    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace('.', '/');
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format("BAD_PACKAGE_ERROR", scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + '.' + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}
