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

import java.lang.annotation.Annotation;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.jboss.weld.context.AbstractContext;
import org.jboss.weld.context.SerializableContextualInstanceImpl;
import org.jboss.weld.context.api.ContextualInstance;
import org.jboss.weld.context.beanstore.BeanStore;
import org.jboss.weld.context.beanstore.HashMapBeanStore;
import org.jboss.weld.exceptions.IllegalArgumentException;
import org.jboss.weld.exceptions.IllegalStateException;
import org.jboss.weld.serialization.spi.ContextualStore;


/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class ClusteredSingletonContext extends AbstractContext {

    //TODO make context per app (unique_id)
   // private static final String CONTEXT_ACTIVE_FLAG = "_CONTEXT_ACTIVE_FLAG";
    private static ReentrantLock creationLock = new ReentrantLock();

    CacheBeanStore beanStore;

    public ClusteredSingletonContext() {
       super(false);
    }

    public Class<? extends Annotation> getScope() {
        return ClusteredSingleton.class;
    }

    public boolean isActive() {
      //  return (Boolean)getCache().get(CONTEXT_ACTIVE_FLAG);
        return beanStore != null;
    }

    @Override
    protected CacheBeanStore getBeanStore() {
        //TODO verify beanStore scope it must be application scoped
        return beanStore;
    }

    public void activate() {
       beanStore = new CacheBeanStore();
     //  setActive(true);
    }

    public void deactivate() {
       cleanup();
     //  setActive(false);
       beanStore=null;
    }

    /**
     * @see org.jboss.weld.context.AbstractContext#get(Contextual, CreationalContext)
     */
    //@SuppressWarnings(value="UL_UNRELEASED_LOCK", justification="False positive from FindBugs")
    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext)
    {
       boolean multithreaded = false;

       if (!isActive())
       {
          throw new ContextNotActiveException();
       }
       if (getBeanStore() == null)
       {
          return null;
       }
       if (contextual == null)
       {
          throw new IllegalArgumentException(CONTEXTUAL_IS_NULL);
       }
       String id = getId(contextual);
       T instance = null;
       ContextualInstance<T> beanInstance = getBeanStore().get(id);
       if (beanInstance != null)
       {
          return beanInstance.getInstance();
       } else if (getBeanStore().getRemoteInstance(id) != null) {
           instance = (T) getBeanStore().getRemoteInstance(id);
       }

       if (creationalContext != null)
       {
          try
          {
             if (multithreaded)
             {
                creationLock.lock();
                beanInstance = getBeanStore().get(id);
                if (beanInstance != null)
                {
                   return beanInstance.getInstance();
                }
             }
             if (instance == null) {
                 instance = contextual.create(creationalContext);
             }
             if (instance != null)
             {
                beanInstance = new SerializableContextualInstanceImpl<Contextual<T>, T>(contextual, instance, creationalContext, getServiceRegistry().get(ContextualStore.class));
                getBeanStore().put(id, beanInstance);
             }
             return instance;
          }
          finally
          {
             if (multithreaded)
             {
                creationLock.unlock();
             }
          }
       }
       else
       {
          return null;
       }
    }


    /**
     * @param b
     */
//    private void setActive(boolean b) {
//        getCache().put(CONTEXT_ACTIVE_FLAG, true);
//    }


    /**
     * @return
     */
    private Cache<String,Object> getCache() {
        return CacheFactory.getCache();
    }


}
