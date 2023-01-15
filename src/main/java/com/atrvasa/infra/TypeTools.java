package com.atrvasa.infra;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class TypeTools {


    public static boolean equalsTypes(Class<?> type, Class<?> comparableType) {
        do {
            if (type.getName().equals(comparableType.getName()))
                return true;
            comparableType = comparableType.getSuperclass();
        } while (comparableType != null);
        return false;
    }

    public static boolean isImplements(Class<?> type, Class<?> comparableType) {
        do {
            if (type.isAssignableFrom(comparableType))
                return true;
            comparableType = comparableType.getSuperclass();
        } while (comparableType != null);
        return false;
    }

    public static <M> M getGenericInstance(Field field) throws IllegalAccessException, InstantiationException {
        Class<M> type = getFieldGenericType(field);
        return type.newInstance();
    }

    public static <M> Class<M> getFieldGenericType(Field field) {
        Class<M> type = null;
        Type genericType = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        if (parameterizedType != null) {
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types != null && types.length > 0)
                type = (Class<M>) types[0];
        }
        return type;
    }


    public static <M> Class<M> getGenericTypeFromSuperClass(Class<?> type, int index) {
        Class<M> result = null;
        ParameterizedType parameterizedType = (ParameterizedType) type.getSuperclass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types != null && types.length > index) {
            if (types[0] instanceof TypeVariableImpl) {
                result = ((TypeVariableImpl<Class<M>>) types[index]).getGenericDeclaration();
            } else {
                result = (Class<M>) types[index];
            }
        }
        return result;
    }

    public static <M> Class<M> getGenericType(Class<?> type, int index) {
        Class<M> result = null;
        ParameterizedType parameterizedType = (ParameterizedType) type.getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types != null && types.length > index) {
            if (types[0] instanceof TypeVariableImpl) {
                result = ((TypeVariableImpl<Class<M>>) types[index]).getGenericDeclaration();
            } else {
                result = (Class<M>) types[index];
            }
        }
        return result;
    }

}
