package cn.iocoder.yudao.module.wms.config;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.autotable.annotation.AutoTable;
import org.dromara.autotable.core.interceptor.BuildTableMetadataInterceptor;
import org.dromara.autotable.core.strategy.ColumnMetadata;
import org.dromara.autotable.core.strategy.TableMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SimpleMybatisPlusInterceptor implements BuildTableMetadataInterceptor {

    // 定义 BaseDO 固定的审计字段名
    private static final Set<String> BASE_DO_FIELDS = Set.of("createTime", "updateTime", "creator", "updater", "deleted");

    @Override
    public void intercept(String databaseDialect, TableMetadata tableMetadata) {
        Class<?> clazz = extractClass(tableMetadata);
        if (clazz == null) {
            return;
        }

        // ==========================================
        // 1. 处理表名：@AutoTable 优先级高，其次 @TableName
        // ==========================================
        String tableName = null;
        AutoTable autoTable = clazz.getAnnotation(AutoTable.class);
        if (autoTable != null && StringUtils.hasText(autoTable.value())) {
            tableName = autoTable.value();
        } else {
            TableName tableNameAnno = clazz.getAnnotation(TableName.class);
            if (tableNameAnno != null && StringUtils.hasText(tableNameAnno.value())) {
                tableName = tableNameAnno.value();
            }
        }
        if (StringUtils.hasText(tableName)) {
            setTableName(tableMetadata, tableName);
        }

        // ==========================================
        // 2. 强制补齐 BaseDO 字段
        // ==========================================
        List<ColumnMetadata> columns = extractColumns(tableMetadata);
        if (columns != null) {
            Set<String> existingFieldNames = columns.stream()
                    .map(col -> (String) getProperty(col, "fieldName"))
                    .collect(Collectors.toSet());

            // 检查继承关系，确保该类确实继承自 BaseDO
            if (BaseDO.class.isAssignableFrom(clazz)) {
                Class<?> currentClass = clazz;
                while (currentClass != null && currentClass != Object.class) {
                    for (Field field : currentClass.getDeclaredFields()) {
                        String fieldName = field.getName();

                        // 如果是 BaseDO 的审计字段，且当前列列表中没有，则强制补入
                        if (BASE_DO_FIELDS.contains(fieldName) && !existingFieldNames.contains(fieldName)) {
                            ColumnMetadata newColumn = createColumnMetadata(field);
                            if (newColumn != null) {
                                columns.add(newColumn);
                                existingFieldNames.add(fieldName);
                            }
                        }
                    }
                    currentClass = currentClass.getSuperclass();
                }
            }
        }
    }

    /**
     * 创建基础列元数据
     */
    private ColumnMetadata createColumnMetadata(Field field) {
        try {
            ColumnMetadata column = ColumnMetadata.class.getDeclaredConstructor().newInstance();

            String fieldName = field.getName();
            setProperty(column, "fieldName", fieldName);
            setProperty(column, "name", camelToUnderline(fieldName));
            setProperty(column, "type", field.getType());

            // 识别 @TableLogic (逻辑删除字段)
            TableLogic tableLogic = field.getAnnotation(TableLogic.class);
            if (tableLogic != null || "deleted".equals(fieldName)) {
                setProperty(column, "defaultValue", "0");
                setProperty(column, "comment", "是否删除(0未删除 1已删除)");
            }

            // 基础审计字段的中文注释
            switch (fieldName) {
                case "createTime": setProperty(column, "comment", "创建时间"); break;
                case "updateTime": setProperty(column, "comment", "最后更新时间"); break;
                case "creator": setProperty(column, "comment", "创建者"); break;
                case "updater": setProperty(column, "comment", "更新者"); break;
                case "deleted":
                    if (getProperty(column, "comment") == null) {
                        setProperty(column, "comment", "是否删除");
                    }
                    break;
            }

            return column;
        } catch (Exception e) {
            return null;
        }
    }

    private String camelToUnderline(String str) {
        if (str == null) return "";
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    // ================== 反射辅助方法 ==================

    private Class<?> extractClass(TableMetadata metadata) {
        Object obj = getProperty(metadata, "clazz");
        if (obj == null) obj = getProperty(metadata, "entityClass");
        if (obj == null) obj = getProperty(metadata, "beanClass");
        return obj instanceof Class ? (Class<?>) obj : null;
    }

    private void setTableName(TableMetadata metadata, String tableName) {
        Field field = ReflectionUtils.findField(metadata.getClass(), "tableName");
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, metadata, tableName);
        }
    }

    private void setProperty(Object target, String fieldName, Object value) {
        if (target == null) return;
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, target, value);
        }
    }

    private Object getProperty(Object target, String fieldName) {
        if (target == null) return null;
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, target);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<ColumnMetadata> extractColumns(TableMetadata metadata) {
        Object cols = getProperty(metadata, "columns");
        if (cols == null) cols = getProperty(metadata, "columnList");
        if (cols == null) cols = getProperty(metadata, "columnMetadataList");
        return cols instanceof List ? (List<ColumnMetadata>) cols : null;
    }
}