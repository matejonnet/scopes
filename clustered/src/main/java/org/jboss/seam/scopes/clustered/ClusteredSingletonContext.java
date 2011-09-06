/**
 *
 */
package org.jboss.seam.scopes.clustered;

import static org.jboss.weld.logging.Category.CONTEXT;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;
import static org.jboss.weld.logging.messages.ContextMessage.CONTEXTUAL_INSTANCE_REMOVED;
import static org.jboss.weld.logging.messages.ContextMessage.CONTEXTUAL_IS_NULL;
import static org.jboss.weld.logging.messages.ContextMessage.CONTEXT_CLEARED;
import static org.jboss.weld.logging.messages.ContextMessage.NO_BEAN_STORE_AVAILABLE;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.Cache;
import org.infinispan.commands.read.GetKeyValueCommand;
import org.infinispan.manager.CacheContainer;
import org.jboss.seam.scopes.common.LookupContext;
import org.jboss.weld.Container;
import org.jboss.weld.bean.ManagedBean;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.context.AbstractContext;
import org.jboss.weld.context.SerializableContextualInstanceImpl;
import org.jboss.weld.context.api.ContextualInstance;
import org.jboss.weld.context.beanstore.BeanStore;
import org.jboss.weld.context.beanstore.HashMapBeanStore;
import org.jboss.weld.exceptions.IllegalStateException;
import org.jboss.weld.serialization.spi.ContextualStore;


/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@Singleton
public class ClusteredSingletonContext implements Context {

    BeanManager beanManager;
    private CacheBeanStore beanStore;
//    private ServiceRegistry serviceRegistry;



        @Inject
        public ClusteredSingletonContext(BeanManager manager) {
            this.beanManager = manager;
//            this.serviceRegistry = Container.instance().services();
            activate();
       }



    /* (non-Javadoc)
     * @see javax.enterprise.context.spi.Context#get(javax.enterprise.context.spi.Contextual)
     */
    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        if (contextual == null) {
            throw new org.jboss.weld.exceptions.IllegalArgumentException(CONTEXTUAL_IS_NULL);
        }
//        if (!(contextual instanceof Bean<?>)) {
//            throw new IllegalArgumentException("Can only handle beans: " + contextual);
//        }
//      if (!(contextual instanceof Serializable)) {
//      throw new IllegalArgumentException("Can only handle serializable beans: " + contextual);
//  }
        if (getBeanStore() == null) {
            return null;
        }

      //  Bean<T> bean = (Bean<T>) contextual;
        String id = getId(contextual);

        ContextualInstance<T> beanInstance = getBeanStore().get(id);
        if (beanInstance != null) {
            return beanInstance.getInstance();
        } else if (creationalContext != null) {

            T instance = contextual.create(creationalContext);
            if (instance != null) {
                //beanInstance = new SerializableContextualInstanceImpl<Contextual<T>, T>(contextual, instance, creationalContext, serviceRegistry.get(ContextualStore.class));
                getBeanStore().put(id, instance);
            }
            return instance;
        }
        return null;
    }

    //@Override
    protected String getId(Contextual<?> contextual) {
        //TODO implement key generation
        return contextual.getClass().toString();
    }


    public Class<? extends Annotation> getScope() {
        return ClusteredSingleton.class;
    }

    public boolean isActive() {
        return beanStore != null;
    }

    protected CacheBeanStore getBeanStore() {
        return beanStore;
    }

    public void activate() {
        beanStore = new CacheBeanStore();
    }

    public void deactivate() {
        cleanup();
        beanStore=null;
    }

    /**
     * @return
     */
    private Cache<String,Object> getCache() {
        return CacheFactory.getCache();
    }

    public void cleanup() {
        if (getBeanStore() != null) {
            getBeanStore().clear();
        }
    }



}
