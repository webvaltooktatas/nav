package hu.webvalto.backend.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@FelsoLimit
public class FelsoLimitInterceptor {
    @AroundInvoke
    public Object kiveteliFelsoLimit(InvocationContext context) throws Exception {
        if ("kivet".equals(context.getMethod().getName())) {
            if ((int) context.getParameters()[0] >= 10000000) {
                throw new IllegalStateException("Sajnos ennyi pénzt nem vehet ki :(");
            }
        }
        return context.proceed();
    }
}
