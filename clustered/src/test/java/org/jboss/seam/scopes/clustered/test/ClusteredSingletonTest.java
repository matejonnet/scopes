package org.jboss.seam.scopes.clustered.test;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.scopes.clustered.CacheFactory;
import org.jboss.seam.scopes.clustered.CacheFactoryJndiProvider;
import org.jboss.seam.scopes.clustered.CacheFactoryLocalProvider;
import org.jboss.seam.scopes.clustered.ClusteredSingleton;
import org.jboss.seam.scopes.clustered.ClusteredSingletonContext;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class ClusteredSingletonTest
{

   @Deployment (name = "dep1") @TargetsContainer("container-A")
   public static Archive myApp() {
      //CacheFactory.init(new CacheFactoryLocalProvider(startInfinispan()));

      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addPackage(ClusteredSingletonTest.class.getPackage())
            .addPackage(ClusteredSingletonContext.class.getPackage())
            .addAsManifestResource("META-INF/beans.xml", "beans.xml")
            .addAsManifestResource("META-INF/jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
            .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", "services/javax.enterprise.inject.spi.Extension");
      System.out.println("archive = " + archive.toString(true));
      return archive;
   }

   @Deployment (name = "dep2") @TargetsContainer("container-B")
   public static Archive myApp2() {
       //CacheFactory.init(new CacheFactoryLocalProvider(startInfinispan()));

       JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
               .addPackage(ClusteredSingletonTest.class.getPackage())
               .addPackage(ClusteredSingletonContext.class.getPackage())
               .addAsManifestResource("META-INF/beans.xml", "beans.xml")
               .addAsManifestResource("META-INF/jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
               .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension", "services/javax.enterprise.inject.spi.Extension");
       System.out.println("archive = " + archive.toString(true));
       return archive;
   }

   @Before
   public void init() {
       CacheFactory.init(new CacheFactoryJndiProvider("java:jboss/infinispan/demo"));
   }

   @Inject
   ClusteredSingletonBean singletonBean;

   @Test @OperateOnDeployment("dep1")
   public void testStep1() throws Exception
   {
       singletonBean.setField1("string1");
   }

   @Test @OperateOnDeployment("dep2")
   public void testStep2() throws Exception
   {
      Assert.assertNotNull(singletonBean);
      Assert.assertEquals("string1", singletonBean.getField1());
      singletonBean.setField1("string2");
   }

   @Test @OperateOnDeployment("dep1")
   public void testStep3() throws Exception {
       Assert.assertEquals("string2", singletonBean.getField1());
       singletonBean.appendToField1("-add");
   }

   @Test @OperateOnDeployment("dep2")
   public void testStep4() throws Exception {
      Assert.assertEquals("string2-add", singletonBean.getField1());
   }


}
