package org.jboss.seam.scopes.clustered.test;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.scopes.clustered.CacheFactory;
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



   @Deployment
   public static Archive myApp() {
       startInfinispan();

      JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addPackage(ClusteredSingletonTest.class.getPackage())
            .addPackage(ClusteredSingletonContext.class.getPackage())
            .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml")
            .addAsResource(EmptyAsset.INSTANCE, "META-INF/services/javax.enterprise.inject.spi.Extension");
      System.out.println("archive = " + archive.toString(true));
      return archive;
   }

   //@Before
   public static void startInfinispan() {
       //Cache<Object, Object> cache = new DefaultCacheManager().getCache();
       EmbeddedCacheManager manager = new DefaultCacheManager();

       Configuration conf = new Configuration();
       conf.setCacheMode(CacheMode.LOCAL);

       manager.defineConfiguration("custom-cache", conf);

       Cache<String,Object> cache;
       cache = manager.getCache("custom-cache");
       simpleCacheTest(cache);
       CacheFactory.init(new CacheFactoryLocalProvider(manager));

       cache = CacheFactory.getCache();
       simpleCacheTest(cache);
       cache.clear();
   }

   private static void simpleCacheTest(Cache cache) {
       // Add a entry
       cache.put("key", "value");
       // Validate the entry is now in the cache
       assertEquals(1, cache.size());
       assertTrue(cache.containsKey("key"));
       // Remove the entry from the cache
       Object v = cache.remove("key");
       // Validate the entry is no longer in the cache
       assertEquals("value", v);
       assertTrue(cache.isEmpty());
   }

   @After
   public void stopInfinispan() {
       //TODO
   }



   @Inject
   ClusteredSingletonBean singletonBean;

   @Test
   public void test() throws Exception
   {
       singletonBean.setField1("string1");
   }


   @Test
   public void testFirstTest() throws Exception
   {
      Assert.assertNotNull(singletonBean);
      Assert.assertNotNull(singletonBean.getField1());
   }
}
