package com.hu.tookit.util;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 断言工具类
 * @author Huxiaozhi
 * @date 2019年10月10日 下午2:56:07
 */
public class AssertUtil extends Assert{
	private AssertUtil() {};

	public static void isEqual(@Nullable Object source, @Nullable Object target, String message) {
		if (!source.equals(target)) {
			throw new IllegalArgumentException(message);
		}
	}
}
