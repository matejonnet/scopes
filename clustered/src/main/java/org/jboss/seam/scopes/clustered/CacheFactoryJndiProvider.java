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
public class CacheFactoryJndiProvider implements CacheFactoryProvider {

    private String jndiName;

    public CacheFactoryJndiProvider(String jndiName) {
        this.jndiName = jndiName;
    }

    /* (non-Javadoc)
     * @see org.jboss.seam.scopes.clustered.CacheFactoryProvider#getContainer()
     */
    public CacheContainer getContainer() {
        try {
            //TODO change cache name
//            return (CacheContainer) new InitialContext().lookup("java:jboss/infinispan/hacontext");
            return (CacheContainer) new InitialContext().lookup(jndiName);
        } catch (NamingException e) {
            //TODO log event
            System.err.println("Ha context requires cache java:jboss/infinispan/hacontext");
            e.printStackTrace();
        }

        return null;
    }

}
