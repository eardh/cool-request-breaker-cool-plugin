package com.huang;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author lei.huang
 * @Description TODO
 **/
public class BreakContext {

	private final AtomicBoolean cracked = new AtomicBoolean(false);

	private static BreakContext instance;

	public static BreakContext newBreakContext() {
		if (instance == null) {
			synchronized (BreakContext.class) {
				if (instance == null) {
					instance = new BreakContext();
				}
			}
		}
		return instance;
	}

	public AtomicBoolean getCracked() {
		return cracked;
	}
}
