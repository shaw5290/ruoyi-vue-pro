//package cn.iocoder.yudao.module.wms.config;
//
//import com.baomidou.mybatisplus.annotation.*;
//import org.dromara.autotable.core.interceptor.BuildTableMetadataInterceptor;
//import org.dromara.autotable.core.strategy.TableMetadata;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ReflectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.lang.reflect.Field;
//import java.util.Iterator;
//import java.util.List;
//
//@Component
//public class MyTableMetadataInterceptor1 implements BuildTableMetadataInterceptor {
//
//    @Override
//    public void intercept(String databaseDialect, TableMetadata tableMetadata) {
//        // 使用反射安全地从 TableMetadata 中获取实体类 Class
//        Class<?> clazz = extractClass(tableMetadata);
//        if (clazz == null) {
//            return;
//        }
//
//        try {
//            // 1. 强制覆盖真实的表名
//            TableName tableNameAnno = clazz.getAnnotation(TableName.class);
//            if (tableNameAnno != null && StringUtils.hasText(tableNameAnno.value())) {
//                setProperty(tableMetadata, "tableName", tableNameAnno.value());
//            }
//
//            // 2. 处理字段注解与属性
//            List<?> columns = extractColumns(tableMetadata);
//            if (columns != null) {
//                Iterator<?> iterator = columns.iterator();
//                while (iterator.hasNext()) {
//                    Object column = iterator.next();
//
//                    String fieldName = (String) getProperty(column, "fieldName");
//                    if (!StringUtils.hasText(fieldName)) fieldName = (String) getProperty(column, "name");
//
//                    Field javaField = ReflectionUtils.findField(clazz, fieldName);
//                    if (javaField == null) continue;
//
//                    // 解析 @TableField (exist=false 直接剔除)
//                    TableField tableField = javaField.getAnnotation(TableField.class);
//                    if (tableField != null) {
//                        if (!tableField.exist()) {
//                            iterator.remove();
//                            continue;
//                        }
//                        if (StringUtils.hasText(tableField.value())) {
//                            setProperty(column, "name", tableField.value());
//                        }
//                    }
//
//                    // 解析 @TableId (设为主键)
//                    TableId tableId = javaField.getAnnotation(TableId.class);
//                    if (tableId != null) {
//                        setProperty(column, "primaryKey", true);
//                        if (StringUtils.hasText(tableId.value())) {
//                            setProperty(column, "name", tableId.value());
//                        }
//                        if (tableId.type() == IdType.AUTO) {
//                            setProperty(column, "autoIncrement", true);
//                        }
//                    }
//
//                    // 解析 @TableLogic (逻辑删除)
//                    TableLogic tableLogic = javaField.getAnnotation(TableLogic.class);
//                    if (tableLogic != null) {
//                        setProperty(column, "defaultValue", "0");
//                        setProperty(column, "comment", "是否删除(0未删除 1已删除)");
//                    }
//
//                    // 补充 BaseDO 基础字段注释
//                    if (fieldName != null) {
//                        switch (fieldName) {
//                            case "createTime": setProperty(column, "comment", "创建时间"); break;
//                            case "updateTime": setProperty(column, "comment", "最后更新时间"); break;
//                            case "creator": setProperty(column, "comment", "创建者"); break;
//                            case "updater": setProperty(column, "comment", "更新者"); break;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // 忽略异常，防止阻断启动
//        }
//    }
//
//    // ================== 反射提取 Class 与属性的通用方法 ==================
//
//    private Class<?> extractClass(TableMetadata metadata) {
//        // 尝试通过常见字段名反射获取 Class
//        Object obj = getProperty(metadata, "clazz");
//        if (obj == null) obj = getProperty(metadata, "entityClass");
//        if (obj == null) obj = getProperty(metadata, "beanClass");
//        if (obj instanceof Class) {
//            return (Class<?>) obj;
//        }
//        return null;
//    }
//
//    private void setProperty(Object target, String fieldName, Object value) {
//        if (target == null) return;
//        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
//        if (field != null) {
//            ReflectionUtils.makeAccessible(field);
//            ReflectionUtils.setField(field, target, value);
//        }
//    }
//
//    private Object getProperty(Object target, String fieldName) {
//        if (target == null) return null;
//        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
//        if (field != null) {
//            ReflectionUtils.makeAccessible(field);
//            return ReflectionUtils.getField(field, target);
//        }
//        return null;
//    }
//
//    private List<?> extractColumns(Object metadata) {
//        Object cols = getProperty(metadata, "columns");
//        if (cols == null) cols = getProperty(metadata, "columnList");
//        if (cols == null) cols = getProperty(metadata, "columnMetadataList");
//        return cols instanceof List ? (List<?>) cols : null;
//    }
//}