/**
 *
 */
package org.jboss.seam.scopes.clustered;

import java.util.Set;

import javax.enterprise.event.Observes;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;


/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class ClusteredContextsExtension implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager manager) {
        registerContext(event, manager);
    }

    public void registerContext(AfterBeanDiscovery event, BeanManager manager) {
        //TODO log
        System.out.println(">>>>> Registering ClusteredContexts ...");
        ClusteredSingletonContext csc = new ClusteredSingletonContext();
        event.addContext(csc);
        csc.activate();
        //TODO deactivate context on shutdown
    }


}
