package com.nio.zrpc.hystrix;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.omg.CORBA.SystemException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.nio.zrpc.core.InvokeService;
import com.nio.zrpc.definition.RpcDefinition;

public class RpcHystrixCommand extends HystrixCommand<Object> {

	private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
	private final RpcDefinition invoker;

	public RpcHystrixCommand(RpcDefinition invoker) {
		// super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getMethodName()))
		// .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		// .withCircuitBreakerRequestVolumeThreshold(20)//10秒钟内至少19此请求失败，熔断器才发挥起作用
		// .withCircuitBreakerSleepWindowInMilliseconds(30000)//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
		// .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
		// .withExecutionTimeoutEnabled(false))//使用rpc的超时，禁用这里的超时
		// .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(DEFAULT_THREADPOOL_CORE_SIZE)));//线程池为30

		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.invoker = invoker;
	}

	@Override
	protected Object run() throws Exception {
		Object result = null;
		//Object result= InvokeService.invokeService(invoker);
		if (result == null) {
			throw new NullPointerException();
		}

		return result;
	}

	@Override
	protected String getFallback() {
		// 本地方法调用
		//{"name":"zhangrp","age":13}
		//System.out.println("===========getFallback==============");
		Object result = null;
		try {
			System.out.println(invoker.getDef().getFallbackClass());
			Class<?> clazz = Class.forName(invoker.getDef().getFallbackClass());
			Method method = clazz.getDeclaredMethod(invoker.getDef()
					.getFallbackMethod());
			Object obj = clazz.newInstance();
			result = method.invoke(obj, null);
			System.out.println("==============fallback========="+result);
			String jsonString = JSON.toJSONString(result);
			return jsonString;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("No fallback available.");
		}
		//return result;
	}

}
