/**
 *
 */
package org.jboss.seam.scopes.clustered;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.infinispan.Cache;
import org.jboss.weld.bean.ManagedBean;


/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@ClusteredSingletonInterceptor
@Interceptor
//TODO ? needs to be serializable ??
public class ClusteredScopeInterceptor implements Serializable {

    private static final long serialVersionUID = -1134835456910673590L;

    @AroundInvoke
    public Object updateCache(InvocationContext ctx) throws Exception {
        System.out.println(">>>>>>> Intercepting bean ... ");
        Object result = ctx.proceed();
        pushToCache(ctx);
        return result;
    }

   @Inject
   BeanManager beanManager;

    /**
     * push new bean state to cache
     */
    private void pushToCache(InvocationContext ctx) {
        Object o = ctx.getTarget();
        Method m = ctx.getMethod();
        ClusteredSingletonContext context = (ClusteredSingletonContext)beanManager.getContext(ClusteredSingleton.class);

        Set<Bean<?>> beans = beanManager.getBeans(o.getClass().getSuperclass());
        for (Bean b : beans) {
            ManagedBean mb = (ManagedBean)b;

            context.getBeanStore().refreshCache(mb.getId(), o);
            System.currentTimeMillis();
        }

        //context.


        //
//        getCache();
    }

//    private Cache<String,Object> getCache() {
//        return CacheFactory.getCache();
//    }

}
