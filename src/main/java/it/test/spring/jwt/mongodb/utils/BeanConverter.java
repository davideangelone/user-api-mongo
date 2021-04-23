package it.test.spring.jwt.mongodb.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

public class BeanConverter {
	
	public static <T> T convert(Object bean, Class<T> resultType) {
		T result = BeanUtils.instantiateClass(resultType);
		BeanUtils.copyProperties(bean, result);
		return result;
	}
	
	public static <T> List<T> convert(List<?> listBean, Class<T> resultType) {
		return listBean
			.stream()
			.map(item -> convert(item, resultType))
			.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}

}
