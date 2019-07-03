package com.l9e.train.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedUtil {

	private static Logger logger = LoggerFactory.getLogger(MemcachedUtil.class);

	private static MemcachedUtil instance = null;

	private static MemCachedClient mcc = null;

	private static int sessionTimeout = 60000;

	private static SockIOPool pool = null;

	private static String poolName = "memsock";

	private boolean isNotConnection = false;

	private static Properties props = null;

	private static Object lock = new Object();

	private static boolean useflag = true;

	public static boolean useMemcache(boolean f) {
		boolean retF = true;
		try {
			synchronized (lock) {
				useflag = f;
			}
		} catch (Exception e) {
			retF = false;
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return retF;
	}

	public static synchronized MemcachedUtil getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					logger.info("获得memcachedUtil实例");
					instance = new MemcachedUtil();
				}
			}
		}
		return instance;
	}

	public MemcachedUtil() {
		InputStream ins = null;
		try {
			if (props == null || props.isEmpty() || pool == null) {
				ins = this.getClass().getClassLoader().getResourceAsStream("memcache.properties");
				props = new Properties();
				props.load(ins);
				logger.debug("memcache_properties=memcache.properties|" + props);
				initSockIOPool(props);
				sessionTimeout = InnerConvertUtil.getIntProperty(props, "sessionTimeout", 1800000);
			}
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				logger.warn("close inputstream error!", e);
			}
		}
		logger.info("初始化memcachedutil");
	}

	private static void initSockIOPool(Properties props) {
		try {
			String serverlist = props.getProperty("serverlist");
			poolName = props.getProperty("poolname", "memsock");
			String[] servers = serverlist.split(",");
			pool = SockIOPool.getInstance(poolName);
			logger.debug("SockIOPool = " + pool);
			pool.setServers(servers);
			pool.setFailover(InnerConvertUtil.getBooleanProperty(props, "failover", true));
			pool.setInitConn(InnerConvertUtil.getIntProperty(props, "initconn", 10));
			pool.setMinConn(InnerConvertUtil.getIntProperty(props, "minconn", 5));
			pool.setMaxConn(InnerConvertUtil.getIntProperty(props, "maxconn", 250));
			pool.setMaintSleep(InnerConvertUtil.getIntProperty(props, "maintsleep", 30));
			pool.setNagle(InnerConvertUtil.getBooleanProperty(props, "nagle", false));
			pool.setSocketTO(InnerConvertUtil.getIntProperty(props, "socketTO", 3000));
			pool.setAliveCheck(InnerConvertUtil.getBooleanProperty(props, "alivecheck", true));
			pool.initialize();
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 
	 * @param id
	 * @param valueMap
	 * @param expiry
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getMapping(String id) {
		long start = System.currentTimeMillis();

		Map map = null;
		try {
			if (useflag) {
				getMemCachedClient();
				map = (Map) mcc.get(id);
			}
			long end = System.currentTimeMillis();
			logger.debug("get M id=" + id + "   map=" + map + " use time=" + (end - start));
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public boolean setMapping(String id, Map valueMap) {
		try {
			return setMapping(id, valueMap, new Date(new Date().getTime() + sessionTimeout));
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param id
	 * @param valueMap
	 * @param expiry
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean setMapping(String id, Map valueMap, Date expiry) {

		long start = System.currentTimeMillis();

		boolean flag = false;
		try {
			if (useflag) {
				getMemCachedClient();

				flag = mcc.set(id, valueMap, expiry);
			}
			long end = System.currentTimeMillis();
			logger.debug("set M id=" + id + "   flag=" + flag + " use time=" + (end - start));
			if (flag == false) {
				logger.error("set M id=" + id + "   flag=" + flag + " use time=" + (end - start));
			}
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return flag;
	}

	public Object getAttribute(String id) {
		long start = System.currentTimeMillis();
		Object obj = null;
		try {
			if (useflag) {
				getMemCachedClient();
				obj = mcc.get(id);
			}
			long end = System.currentTimeMillis();
			logger.debug("get id=" + id + "  obj=" + obj + " use time=" + (end - start));
			// if (obj == null) {
			// logger.error("MemcachedUtil 取�1�7�失败！");
			// }
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @param id
	 * @param obj
	 * @param expireTime
	 * @return
	 */
	public boolean setAttribute(String id, Object obj, long expireTime) {
		long start = System.currentTimeMillis();
		boolean flag = false;
		try {
			if (useflag) {
				getMemCachedClient();
				if (expireTime > 0)
					flag = mcc.set(id, obj, new Date(expireTime));
				else
					flag = mcc.set(id, obj);
			}
			long end = System.currentTimeMillis();
			logger.debug("set id=" + id + "   flag=" + flag + " use time=" + (end - start));
			if (flag == false) {
				logger.error("set id=" + id + "   flag=" + flag + " use time=" + (end - start));
				logger.error("MemcachedUtil 设置值失败！");
			}
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return flag;
	}

	public boolean setAttribute(String id, Object obj) {
		return this.setAttribute(id, obj, new Date().getTime() + sessionTimeout);
	}

	public boolean removeAttribute(String id) {
		try {
			getMemCachedClient();
			return mcc.delete(id);
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map stats() {
		try {
			getMemCachedClient();
			return mcc.stats();
		} catch (Exception e) {
			logger.info("ErrorMsg=EXCEPTION_Memcached | Timestamp=" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
			e.printStackTrace();
		}
		return null;
	}

	private static void getMemCachedClient() {

		if (mcc == null) {
			mcc = new MemCachedClient();
			mcc.setPoolName(poolName);

			mcc.setCompressEnable(false);
			mcc.setCompressThreshold(0);
		}
	}

	protected void finalize() {
		if (pool != null) {
			pool.shutDown();
		}
	}

	public boolean isNotConnection() {
		return isNotConnection;
	}

	protected static class InnerConvertUtil {

		/**
		 * 根据key取到属�1�7��1�7�1�7
		 * 
		 * @param props
		 * @param key
		 * @return
		 */
		public static int getIntProperty(Properties props, String key) {
			return getIntProperty(props, key, 0);
		}

		/**
		 * 根据key值了 倄1�7 ，如果没有属性文件中没有值，则的返回默认倄1�7
		 * 
		 * @param props
		 * @param key
		 * @param defaultValue
		 * @return
		 */
		public static int getIntProperty(Properties props, String key, int defaultValue) {
			int result = defaultValue;
			if (props != null) {
				String value = props.getProperty(key);
				try {
					result = Integer.parseInt(value);
				} catch (Exception ex) {
					logger.error("get property fail:" + ex.getMessage());
				}
			}
			return result;
		}

		/**
		 * 根据key值返回long型�1�7�1�7
		 * 
		 * @param props
		 * @param key
		 * @return
		 */

		public static long getLongProperty(Properties props, String key) {
			return getLongProperty(props, key, 0);
		}

		/**
		 * 根据key返回long值，若属性文件中没有相应的�1�7�，则返回默认�1�7�1�7
		 * 
		 * @param props
		 * @param key
		 * @param defaultValue
		 * @return
		 */
		public static long getLongProperty(Properties props, String key, long defaultValue) {
			long result = defaultValue;
			if (props != null) {
				String value = props.getProperty(key);
				try {
					result = Long.parseLong(value);
				} catch (Exception ex) {
					logger.warn("get property fail:" + ex.getMessage());
				}
			}
			return result;
		}

		/**
		 * 根据key值从属�1�7�文件取倄1�7
		 * 
		 * @param props
		 * @param key
		 * @return
		 */
		public static boolean getBooleanProperty(Properties props, String key) {
			return getBooleanProperty(props, key, false);
		}

		/**
		 * 根据key取�1�7�，若属性文件中没有相应的�1�7�，则返回默认的倄1�7
		 * 
		 * @param props
		 * @param key
		 * @param defaultValue
		 * @return
		 */
		public static boolean getBooleanProperty(Properties props, String key, boolean defaultValue) {
			boolean result = defaultValue;
			if (props != null) {
				String value = props.getProperty(key);
				try {
					result = Boolean.parseBoolean(value);
				} catch (Exception ex) {
					logger.warn("get property fail:" + ex.getMessage());
				}
			}
			return result;
		}

	}

}
