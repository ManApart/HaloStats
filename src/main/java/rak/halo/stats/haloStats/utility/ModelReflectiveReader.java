package rak.halo.stats.haloStats.utility;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Recursively prints ALL fields (including private) on an object with the @ReflectivePrint annotation, and any sub objects that have the same annotation
 */
public class ModelReflectiveReader {

	public static <T> String toString(T object) {
		String message = "";
		if (object != null){
			if (isArrayObject(object)){
				message = printArrayObject(object);
			} else {
				message = printFields(object);
			}
		}
		return message;
	}

	public static <T> boolean isArrayObject(T object) {
		return object.getClass().isArray();
	}

	public static <T> String printArrayObject(T object) {
		String message = "";
		for (T o : unpack(object)){
			message += toString(o);
		}
		return message;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] unpack(T arrayObject) {
	    Object[] unpackedArray = new Object[Array.getLength(arrayObject)];
	    for(int i=0; i<unpackedArray.length; i++){
	    	unpackedArray[i] = Array.get(arrayObject, i);
	    }
	    return (T[]) unpackedArray;
	}
	
	public static <T> String printFields(T object) {
		String message = "";
		
		for (Field field : getAllFields(object)) {
			message += fieldToString(object, field);
		}
		return message;
	}

	public static <T> List<Field> getAllFields(T object) {
		List<Field> list = new ArrayList<>();
		Class<?> current = object.getClass();
		list.addAll(Arrays.asList(current.getDeclaredFields()));
		
		while (current.getSuperclass() != null)	{
			current = current.getSuperclass();
			list.addAll(Arrays.asList(current.getDeclaredFields()));
		}
		return list;
	}

	private static <T> String fieldToString(T object, Field field) {
		try {
			return attemptFieldToString(object, field);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static <T> String attemptFieldToString(T object, Field field) throws IllegalAccessException {
		field.setAccessible(true);
		Object value = field.get(object);
		
		if (shouldRecursivelyPrint(value)){
			return toString(value);
		} else if(value != null){
			return field.getName() + ": " + value + "\n";
		} else {
			return "";
		}
	}

	private static <T> boolean shouldRecursivelyPrint(T object) {
		return object != null 
			&& (object.getClass().isAnnotationPresent(ReflectivePrint.class)
			|| isArrayObject(object));
	}


}
