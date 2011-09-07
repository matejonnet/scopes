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
public class CacheBeanStore {

    private static final long serialVersionUID = -4244132750844284678L;

    Map<String, Object> proxyStore = new HashMap<String, Object>();

//    public <T> ContextualInstance<T> get(String key) {
//        return (ContextualInstance<T>) getCache().get(key);
//    }
//    public <T> T get(String key) {
//        return (T) getCache().get(key);
//    }
    public Object get(String key) {
        return getCache().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstanceFromCache(String key) {
        return (T) getCache().get(key);
    }

    public <T> void put(String id, T instance) {
        getCache().put(id, instance);
    }

    public void refreshCache(String id, Object value) {
        getCache().put(id, value);
    }

    public boolean contains(String key) {
        return getCache().containsKey(key);
    }

    public void clear() {
        getCache().clear();
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