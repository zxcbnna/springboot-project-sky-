package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution (* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("正在自动填充");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autofill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autofill.value();

        Object[] args = joinPoint.getArgs();

        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();
        Long userID = BaseContext.getCurrentId();


        if(operationType == OperationType.INSERT){
            try{
                Method createTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method createUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method updateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method updateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                createTime.invoke(entity, now);
                createUser.invoke(entity, userID);
                updateTime.invoke(entity, now);
                updateUser.invoke(entity, userID);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            try{
                Method updateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method updateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                updateTime.invoke(entity, now);
                updateUser.invoke(entity, userID);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

}
