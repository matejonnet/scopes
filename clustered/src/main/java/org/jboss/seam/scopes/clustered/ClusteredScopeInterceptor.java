/**
 *
 */
package org.jboss.seam.scopes.clustered;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.infinispan.Cache;


/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@Interceptor
@ClusteredSingleton
public class ClusteredScopeInterceptor {

    @AroundInvoke
    public Object updateCache(InvocationContext ctx) throws Exception {
        Object result = ctx.proceed();
        pushToCache(ctx);
        return result;
    }

    /**
     * @param ctx
     *
     */
    private void pushToCache(InvocationContext ctx) {
        Object o = ctx.getTarget();
        //TODO getCache();
    }

    private Cache<String,Object> getCache() {
        return CacheFactory.getCache();
    }

}
