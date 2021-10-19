package it.test.spring.jwt.mongodb.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanConverter {
	
	private BeanConverter() {
	}
	
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
	
	public static void copyNonNullProperties(Object source, Object destination){
		BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
	}
		  
	/**
	  * Returns an array of null properties of an object
	  * @param source
	  * @return
	  */
	private static String[] getNullPropertyNames (Object source) {
		BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();
		Set<String> emptyNames = new HashSet<>();
		
		for(PropertyDescriptor pd : pds) {
			//check if value of this property is null then add it to the collection
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

}
