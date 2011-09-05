package org.jboss.seam.scopes.clustered.test;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.scopes.clustered.ClusteredSingleton;
import org.jboss.seam.scopes.clustered.ClusteredSingletonInterceptor;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@ClusteredSingleton
@ClusteredSingletonInterceptor
public class ClusteredSingletonBean
{
    private String field1;

    private Map<String, String> map1;

    /**
     * @return the field1
     */
    public String getField1() {
        return field1;
    }

    /**
     * @param field1 the field1 to set
     */
    public void setField1(String field1) {
        this.field1 = field1;
    }

    /**
     * @return the map1
     */
    public Map<String, String> getMap1() {
        return map1;
    }

    /**
     * @param map1 the map1 to set
     */
    public void setMap1(Map<String, String> map1) {
        this.map1 = map1;
    }

    public void putToMap1(String key, String value) {
        map1.put(key, value);
    }

    public void appendToField1(String append) {
        field1 += append;
    }
}
