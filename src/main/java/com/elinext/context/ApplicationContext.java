package com.elinext.context;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static ApplicationContext INSTANCE;

    public static ApplicationContext getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new ApplicationContext();
        }
        return INSTANCE;
    }

    private ApplicationContext(){

    }

    private final Map<Class, Object> container = new HashMap<>();
    private final Map<Class, Object> singletonContainer = new HashMap<>();

    public Object getContainerObjByClass(Class clazz){
        return container.get(clazz);
    }

    public void putObjectIntoContainer(Class clazz, Object object){
        container.put(clazz, object);
    }

    public boolean containsObj(Object object){
        return container.containsValue(object);
    }

    public Object getSingletonContainerObjByClass(Class clazz){
        return singletonContainer.get(clazz);
    }

    public void putSingletonObjectIntoContainer(Class clazz, Object object){
        singletonContainer.put(clazz, object);
    }

    public boolean containsSingletonObj(Object object){
        return singletonContainer.containsValue(object);
    }
}
