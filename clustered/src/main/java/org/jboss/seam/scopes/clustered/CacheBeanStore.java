/**
 *
 */
package org.jboss.seam.scopes.clustered;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.spi.Bean;

import org.infinispan.Cache;
import org.jboss.weld.context.SerializableContextualInstanceImpl;
import org.jboss.weld.context.api.ContextualInstance;
import org.jboss.weld.context.beanstore.BeanStore;
import org.jboss.weld.serialization.spi.helpers.SerializableContextual;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class CacheBeanStore implements BeanStore, Serializable {

    private static final long serialVersionUID = -4244132750844284678L;

    private Map<String, ContextualInstance> contextualInstancesRegistry = new HashMap<String, ContextualInstance>();

    public <T> ContextualInstance<T> get(String key) {
        ContextualInstance<T> ci = contextualInstancesRegistry.get(key);
        if (ci == null) {
            return null;
        }
        return new SerializableContextualInstanceImpl((SerializableContextual) ci.getContextual(), getInstanceFromCache(key),ci.getCreationalContext());
    }

    public Object getInstanceFromCache(String key) {
        return getCache().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> void put(String id, ContextualInstance<T> contextualInstance) {
        //TODO verify if wrapped
        //map.put(id, unwrap((ProxyValue)contextualInstance));
        getCache().put(id, contextualInstance.getInstance());
        contextualInstancesRegistry.put(id, contextualInstance);
    }

    public void refreshCache(String id, Object value) {
        getCache().put(id, value);
    }



    public boolean contains(String key) {
        //TODO reload from cache ?
        return getCache().containsKey(key);
    }

    public void clear() {
        // TODO push to cache
        getCache().clear();
        contextualInstancesRegistry.clear();
    }

    public Iterator<String> iterator() {
        //return contextualInstances.keySet().iterator();
        for (String key : getCache().keySet()) {
            //TODO filter only this app
        }
        return getCache().keySet().iterator();
    }

    private Cache<String,Object> getCache() {
        return CacheFactory.getCache();
    }


    private String getAppUniqueId() {
        //TODO provide application unique id
        String key = "application_uniqu_id";
        return key;
    }





}