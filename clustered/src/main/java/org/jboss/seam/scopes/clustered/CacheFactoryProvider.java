/**
 *
 */
package org.jboss.seam.scopes.clustered;

import org.infinispan.manager.CacheContainer;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public interface CacheFactoryProvider {
    public CacheContainer getContainer();
}
