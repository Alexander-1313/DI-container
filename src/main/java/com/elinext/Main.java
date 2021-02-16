package com.elinext;

import com.elinext.container.Injector;
import com.elinext.container.Provider;
import com.elinext.container.impl.InjectorImpl;
import com.elinext.dao.Event;
import com.elinext.dao.EventDao;
import com.elinext.dao.impl.EventDaoImpl;
import com.elinext.dao.impl.EventImpl;
import com.elinext.exception.BindingNotFoundException;
import com.elinext.exception.ConstructorNotFoundException;
import com.elinext.exception.TooManyConstructorsException;

import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, ConstructorNotFoundException, TooManyConstructorsException, BindingNotFoundException {

        Injector injector = InjectorImpl.getInstance();
        injector.bindSingleton(Event.class, EventImpl.class);
        injector.bind(EventDao.class, EventDaoImpl.class);

        Provider<EventDao> provider = injector.getProvider(EventDao.class);
        EventDao instance = provider.getInstance();
        instance.printMessage();
        System.out.println("((EventDaoImpl)instance).getE() = " + ((EventDaoImpl) instance).getE());
    }

}
