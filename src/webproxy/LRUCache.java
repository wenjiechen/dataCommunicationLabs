package webproxy;

import java.net.URL;

import org.apache.lucene.util.DoubleBarrelLRUCache;
import org.apache.lucene.util.DoubleBarrelLRUCache.CloneableKey;

public class LRUCache {
  DoubleBarrelLRUCache<URLkey, byte[]> cache;

  public LRUCache(int size) {
    cache = new DoubleBarrelLRUCache<URLkey, byte[]>(size);
  }

  class URLkey extends DoubleBarrelLRUCache.CloneableKey {
    URL url;

    public URLkey(URL url) {
      this.url = url;
    }

    @Override
    public CloneableKey clone() {
      return new URLkey(url);
    }
  }

  public void put(URLkey url, byte[] value) {
    cache.put(url, value);
  }

  public byte[] get(URLkey url) {
    return cache.get(url);
  }
}
