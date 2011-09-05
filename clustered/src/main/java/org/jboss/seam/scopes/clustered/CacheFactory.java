/**
 *
 */
package org.jboss.seam.scopes.clustered;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class CacheFactory {

    private static CacheContainer container;
    private static CacheFactoryProvider provider;

    public static void init(CacheFactoryProvider _provider) {
        provider = _provider;
    }

    public static Cache<String, Object> getCache() {
        if (container == null) {
            container = provider.getContainer();
        }
        return container.getCache();
    }

}
