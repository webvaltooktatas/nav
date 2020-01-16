package hu.webvalto.backend.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Loggable
public class LoggerInterceptor {
    @AroundInvoke
    public Object doLog(InvocationContext context) throws Exception {
        Logger logger = LoggerFactory.getLogger(context.getClass());
        logger.info("Hivott metodus: " + context.getMethod().getName());
        logger.info("Parameterei: " + context.getParameters().toString());
        Object result = context.proceed();
        logger.info("Hivott metodus veget ert: " + context.getMethod().getName());
        return result;
    }
}
