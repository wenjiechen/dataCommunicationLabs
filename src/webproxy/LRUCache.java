package webproxy;

import org.apache.lucene.util.DoubleBarrelLRUCache;
import org.apache.lucene.util.DoubleBarrelLRUCache.CloneableKey;

public class LRUCache {
  DoubleBarrelLRUCache lrucache = new DoubleBarrelLRUCache(100);
  
  
  class URLkey extends DoubleBarrelLRUCache.CloneableKey{   
    @Override
    public CloneableKey clone() {
      // TODO Auto-generated method stub
      return null;
    }
    
  }
  
  
}
