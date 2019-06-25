package com.mornsnow.common.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianyang
 */
public class BeanUtils {

    /**
     * 转换单个对象
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     */
    public static <S, T> void convertBean(S source, T target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    /**
     * 转换单个对象
     *
     * @param source
     * @param clazz
     * @param <S>
     * @param <T>
     */
    public static <S, T> T copyBean(S source, Class<T> clazz) {

        try {
            Object o = clazz.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, o);
            return (T) o;
        } catch (Exception e) {
            throw new RuntimeException("copy bean faild");
        }

    }

    /**
     * 转换对象列表，要求
     *
     * @param source
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> ArrayList<T> convertList(List<S> source, final Class<T> targetClass) {
        return convertList(source, new ObjectCreator<T>() {
            @Override
            public T createObj() {
                try {
                    return targetClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Error copy properties.", e);
                }
            }
        });
    }

    public static <S, T> ArrayList<T> convertList(List<S> source, ObjectCreator<T> targetObjectCreator) {
        ArrayList<T> target = new ArrayList<T>();
        if (CollectionUtils.isEmpty(source)) {
            return target;
        }
        for (S sourceEntity : source) {
            T targetEntity = targetObjectCreator.createObj();
            convertBean(sourceEntity, targetEntity);
            target.add(targetEntity);
        }
        return target;
    }

    public interface ObjectCreator<T> {
        T createObj();
    }
}
