package com.npf.cloud.monitor.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.monitor.cat
 * @ClassName: CatMonitorAspect
 * @Author: ningpf
 * @Description: cat监控
 * @Date: 2020/3/15 16:20
 * @Version: 1.0
 */
@Aspect
@Order(5)
@Configuration
@ConfigurationProperties("classpath:META-INF/app.properties")
public class CatMonitorAspect {

    private static final String CAT_TRANSACTION_TYPE_REST="controller";
    private static final String CAT_TRANSACTION_TYPE_SERVICE="service";
    private static final String CAT_TRANSACTION_TYPE_DAO="dao";
    private static final String CAT_TRANSACTION_TYPE_CLIENT="client";
    private static final String CAT_TRANSACTION_TYPE_CONSUMER="consumer";
    private static final String CAT_TRANSACTION_TYPE_PRODUCER="producer";


    private Object doMonitor(ProceedingJoinPoint proceedingJoinPoint, String transactionType) throws Throwable {
        String canonicalName = proceedingJoinPoint.getTarget().getClass().getCanonicalName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Transaction transaction = Cat.newTransaction(transactionType, canonicalName + "." + methodName);
        Object object;
        try {
            object = proceedingJoinPoint.proceed();
            transaction.setStatus(Transaction.SUCCESS);
        }catch (Throwable throwable) {
            Cat.logError(throwable);
            transaction.setStatus(throwable);
            throw  throwable;
        }finally {
            if(transaction != null)
                transaction.complete();
        }
        return object;
    }

    @Around(value = "catRestMonitorPointCut()")
    public Object doRestAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_REST);
    }

    @Around(value = "catServiceMonitorPointCut()")
    public Object doServiceAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_SERVICE);
    }

    @Around(value = "catDaoMonitorPointCut()")
    public Object doDaoAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_DAO);
    }

    @Around(value = "catClientMonitorPointCut()")
    public Object doClientAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_CLIENT);
    }

    @Around(value = "catConsummerMonitorPointCut()")
    public Object doConsumerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_CONSUMER);
    }

    @Around(value = "catProducerMonitorPointCut()")
    public Object doProducerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return doMonitor(proceedingJoinPoint,CAT_TRANSACTION_TYPE_PRODUCER);
    }

    @Pointcut(value = "execution(* com.npf.cloud..controller..*Controller.*(..))")
    public void catRestMonitorPointCut() {
    }

    @Pointcut(value = "execution(* com.npf.cloud..service..*Service.*(..))" )
    public void catServiceMonitorPointCut() {
    }

    @Pointcut(value = "execution(* com.npf.cloud..dao..*Mapper.*(..))")
    public void catDaoMonitorPointCut() {
    }

    @Pointcut(value = "execution(* com.npf.cloud..client..*Client.*(..))")
    public void catClientMonitorPointCut() {
    }

    @Pointcut(value = "execution(* com.npf.cloud..consumer..*Consumer.*(..))")
    public void catConsummerMonitorPointCut() {
    }

    @Pointcut(value = "execution(* com.npf.cloud..producer..*MessageProducer.*(..))")
    public void catProducerMonitorPointCut() {
    }

}
