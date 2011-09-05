/**
 *
 */
package org.jboss.seam.scopes.clustered;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.manager.CacheContainer;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class CacheFactoryLocalProvider implements CacheFactoryProvider {

    CacheContainer container;

    public CacheFactoryLocalProvider(CacheContainer container) {
        this.container = container;
    }

    /* (non-Javadoc)
     * @see org.jboss.seam.scopes.clustered.CacheFactoryProvider#getContainer()
     */
    public CacheContainer getContainer() {
        return container;
    }

}
