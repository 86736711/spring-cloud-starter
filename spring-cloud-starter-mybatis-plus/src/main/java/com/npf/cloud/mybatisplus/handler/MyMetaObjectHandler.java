package com.npf.cloud.mybatisplus.handler;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.npf.cloud.pkgeneration.KeyGenerator;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class MyMetaObjectHandler implements MetaObjectHandler {

    KeyGenerator keyGenerator;


    private final static String FIELD_CREATE_TIME = "createTime";

    private final static String FIELD_MODIFY_TIME = "modifyTime";


    public MyMetaObjectHandler(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }


    private void autoFillTableKey(MetaObject metaObject) {
        List<Field> fieldList = ReflectionKit.getFieldList(ClassUtils.getUserClass(metaObject.getOriginalObject().getClass()));
        if (CollectionUtils.isNotEmpty(fieldList)) {
            List<Field> filterList  =fieldList.stream()
                    .filter(i -> {
                        /* 过滤非主键注解属性 */
                        TableId tableId = i.getAnnotation(TableId.class);
                        return (tableId != null);
                    }).collect(toList());


            for (Field f : filterList) {

                final Object tableKey = getFieldValByName(f.getName(), metaObject);
                if (null == tableKey) {
                   setFieldValByName(f.getName(), keyGenerator.generateKey(),metaObject);
                }

            }
        }



    }

    @Override
    public void insertFill(MetaObject metaObject) {

        //1.自动填充主键
        autoFillTableKey(metaObject);


        final Object createTime = getFieldValByName(FIELD_CREATE_TIME, metaObject);

        if (null == createTime) {
        setFieldValByName(FIELD_CREATE_TIME, new Date(),    metaObject);
        }

        final Object modifyTime = getFieldValByName(FIELD_MODIFY_TIME, metaObject);

        if (null == modifyTime) {
            setFieldValByName(FIELD_MODIFY_TIME, new Date(),metaObject);
        }



    }

    @Override
    public void updateFill(MetaObject metaObject) {

        final Object modifyTime = getFieldValByName(FIELD_MODIFY_TIME, metaObject);

        if (null == modifyTime) {
            setFieldValByName(FIELD_MODIFY_TIME, new Date(),metaObject);
        }

    }

}