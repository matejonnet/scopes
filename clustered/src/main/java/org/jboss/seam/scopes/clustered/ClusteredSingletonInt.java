/**
 *
 */
package org.jboss.seam.scopes.clustered;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;
import javax.interceptor.InterceptorBinding;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */

@Target( { TYPE, METHOD})
@Retention(RUNTIME)
@InterceptorBinding
public @interface ClusteredSingletonInt {
}
