package com.elinext.container;

import com.elinext.exception.BindingNotFoundException;
import com.elinext.exception.ConstructorNotFoundException;
import com.elinext.exception.TooManyConstructorsException;

import java.lang.reflect.InvocationTargetException;

public interface Injector {

    //получение инстанса класса со всеми иньекциями по классу интерфейса
    <T> Provider<T> getProvider(Class<T> type) throws BindingNotFoundException, ConstructorNotFoundException, TooManyConstructorsException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException;

    //регистрация байндинга по классу интерфейса и его реализации
    <T> void bind(Class<T> intf, Class<? extends T> impl) throws IllegalAccessException, InvocationTargetException, InstantiationException, TooManyConstructorsException, BindingNotFoundException, ConstructorNotFoundException;

    //регистрация синглтон класса
    <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) throws BindingNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, TooManyConstructorsException, ConstructorNotFoundException;
}
