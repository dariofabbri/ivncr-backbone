package it.dariofabbri.ivncr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

public class MappingUtil<T> {

	public List<T> map(Collection<?> source, Class<T> clazz) {
		
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
		
		List<T> result = new ArrayList<T>();
		for(Object o : source) {
			
			T t = mapper.map(o, clazz);
			result.add(t);
		}
		
		return result;
	}
	
	
}
