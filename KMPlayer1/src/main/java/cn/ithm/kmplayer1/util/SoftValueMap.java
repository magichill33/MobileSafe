package cn.ithm.kmplayer1.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 降低对象的引用级别到软引用
 * 
 * @author Administrator
 * 
 * @param <K>
 * @param <V>
 */
public class SoftValueMap<K, V> extends HashMap<K, V> {
	// 降低V的引用级别到软引用
	private HashMap<K, SoftValue<K, V>> temp;

	private ReferenceQueue<V> queue;// 装破袋子（放置手机V）

	public SoftValueMap() {
		// 软引用
		// Object v=new Object();//占用内存多
		// SoftReference sr=new SoftReference(v);// 降低了v的引用界别

		// ①将手机（占用内存较多的对象）添加到袋子（SoftReference）里面
		// ②一旦手机被偷了，将破袋子回收

		temp = new HashMap<K, SoftValue<K, V>>();
		queue = new ReferenceQueue<V>();
	}

	@Override
	public V put(K key, V value) {
		// SoftValue<K,V> sr = new SoftValue<K,V>(value);
		// 当GC在回收手机时候，会将sr添加到queue
		SoftValue<K, V> sr = new SoftValue<K, V>(value, key, queue);// 将sr与queue绑定
		temp.put(key, sr);
		return null;
	}

	@Override
	public V get(Object key) {
		clearSR();
		SoftValue<K, V> sr = temp.get(key);
		if (sr != null) {
			// 如果此引用对象已经由程序或垃圾回收器清除，则此方法将返回 null。
			return sr.get();
		} else {
			return null;
		}
	}

	@Override
	public boolean containsKey(Object key) {
		clearSR();
		// 什么才叫含有
		// 获取到袋子，从袋子里面拿到手机了，含有
		SoftValue<K, V> sr = temp.get(key);
		// temp.containsKey(key);

		/*
		 * if(sr.get()!=null) { return true; }else{ return false; }
		 */
		if (sr != null) {
			return sr.get() != null;
		}
		return false;

	}

	/**
	 * 清理被回收掉手机的破袋子
	 */
	private void clearSR() {
		// 方案一：循环一下temp，如果被回收掉了，清理出ｔｅｍｐ
		// 内存如果内存还能够满足：temp不会有元素被回收

		// 方案二：如果GC把手机回收，将破袋子的引用记录到一个（自己创建）集合中
		// （自己创建）集合

		// 如果存在一个立即可用的对象，则从该队列中"移除"此对象并返回。否则此方法立即返回 null。
		SoftValue<K, V> poll = (SoftValue<K, V>) queue.poll();
		while (poll != null) {
			// 从temp将sr强引用对象清除
			temp.remove(poll.key);
			poll = (SoftValue<K, V>) queue.poll();
		}
	}

	@Override
	public void clear() {
		temp.clear();
	}

	/**
	 * 加强版的袋子，存储了一下key信息
	 * 
	 * @author Administrator
	 * 
	 * @param <K>
	 * @param <V>
	 */
	private class SoftValue<K, V> extends SoftReference<V> {
		private Object key;

		public SoftValue(V r, Object key, ReferenceQueue<? super V> q) {
			super(r, q);
			this.key = key;
		}
	}

}
