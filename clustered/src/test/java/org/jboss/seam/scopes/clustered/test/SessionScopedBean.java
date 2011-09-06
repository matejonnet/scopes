/**
 *
 */
package org.jboss.seam.scopes.clustered.test;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@SessionScoped
public class SessionScopedBean implements Serializable {

    private static final long serialVersionUID = -1946801143814246624L;

    private String field1;

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
}
