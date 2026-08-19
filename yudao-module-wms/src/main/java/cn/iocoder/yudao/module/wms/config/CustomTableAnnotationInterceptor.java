//package cn.iocoder.yudao.module.wms.config;
//
//import com.baomidou.mybatisplus.annotation.TableName;
//import org.dromara.autotable.core.interceptor.AutoTableAnnotationInterceptor;
//import org.springframework.stereotype.Component;
//
//import java.lang.annotation.Annotation;
//import java.util.Set;
//
///**
// * 注册 MyBatis-Plus 的 @TableName 注解给 AutoTable 识别
// */
//@Component
//public class CustomTableAnnotationInterceptor implements AutoTableAnnotationInterceptor {
//
//    @Override
//    public void intercept(Set<Class<? extends Annotation>> include,
//                          Set<Class<? extends Annotation>> exclude) {
//        // 让 AutoTable 识别标记了 @TableName 的实体类
//        include.add(TableName.class);
//    }
//}