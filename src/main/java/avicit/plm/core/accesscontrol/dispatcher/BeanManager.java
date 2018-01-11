package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.QueryParam;
import avicit.plm.core.accesscontrol.dispatcher.annotation.RequestBody;
import avicit.plm.core.accesscontrol.dispatcher.annotation.RequestHeader;
import avicit.plm.core.accesscontrol.dispatcher.annotation.RunIt;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BeanManager {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private WeakHashMap<String, Object> cache = new WeakHashMap<String, Object>();

    /**
     * retrieve a bean from the bean context
     *
     * @param className the class name
     * @return the bean
     * @throws ClassNotFoundException
     */
    public Object getBean(String className) throws ClassNotFoundException {

        if (cache.containsKey(className))
            return cache.get(className) ;
        else {
            Class aClass = getClass().getClassLoader().loadClass(className);
            AutowireCapableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            Object newBean = beanFactory.createBean(aClass,AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            beanFactory.initializeBean(newBean, className);
            cache.put(className, newBean) ;
            return  newBean ;
        }
    }

    /**
     * get the method annotated with {@RunIt}
     * @param obj the object with methods annotated with {@RunIt}}
     * @param match the string to match
     * @return
     */
    public Method getMethod(Object obj, String match) {
        Method foundMethod = null ;
        for (Method method : obj.getClass().getDeclaredMethods()) {
            // check method name match
            String methodName = method.getName() ;
            if (match.equalsIgnoreCase(methodName)) {
                foundMethod  = method ;
                break;
            }

            // check match definition with annotation
            RunIt runIt = method.getAnnotation(RunIt.class);
            if ( runIt == null)
                continue;
            Pattern pattern = Pattern.compile(runIt.value());
            Matcher matcher = pattern.matcher(match);
            boolean found = matcher.find();
            if (found) {
                foundMethod = method;
                break;
            }
        }
        return foundMethod ;
    }

    /**
     * Invoke a target object that its type is of targetClass
     *
     * @param targetClass the class to instantiate
     * @param criteria the criteria to select the method to invoke
     * @param body the paramet data
     * @param queryStringMap query string
     * @return the value returned by the invoked method
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object invokeTarget(String targetClass, String criteria, String body, Map<String, String> queryStringMap, Map<String, String> headerMap)
            throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        Object targetObj = getBean(targetClass) ;
        Method method = getMethod(targetObj, criteria) ;
        Object paramObject = null ;
        paramObject = deserializeByMethodParameterType(body, method,queryStringMap, headerMap) ;
        Object retObj = invoke(targetObj, method, paramObject) ;
        return retObj ;
    }

    /**
     * invoke the target
     *
     * @param instance the target instance
     * @param method the method of this target to invoke
     * @param methodArg the method arguments
     * @return what the method returned
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invoke(Object instance, Method method, Object methodArg) throws InvocationTargetException, IllegalAccessException {
        Object retObj = null ;
        if ( methodArg == null )
            retObj = method.invoke(instance);
        else
            retObj = method.invoke(instance, (Object[])methodArg) ;
        return retObj;
    }

    /**
     * Convert a json string to Java POJO based on one of the method parameter
     *
     * @param value the valus string
     * @param method the method
     * @param queryStringMap
     * @param headerMap
     * @return
     */
    private Object[] deserializeByMethodParameterType(String value, Method method, Map<String, String> queryStringMap, Map<String, String> headerMap) throws IOException {
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        Annotation[][] methodParameteAsnnotations = method.getParameterAnnotations();
        Object[] methodParameterInstance = new Object[methodParameterTypes.length];
        for (int i = 0; i < methodParameterTypes.length; i++) {
            Class<?> parameterType = methodParameterTypes[i];
            Annotation[] annotations = methodParameteAsnnotations[i];
            if (annotations.length > 1)
                throw new RuntimeException("Invalid annotation - " + method); // TODO
            if (annotations.length == 0)
                methodParameterInstance[i] = toObjectOfParameterType(value, method, i, null) ;
            else {
                Annotation knownAnnotation = annotations[0];
                if (knownAnnotation instanceof RequestBody)
                    methodParameterInstance[i] = toObjectOfParameterType(value, method, i, null);
                else if (knownAnnotation instanceof QueryParam) {
                    QueryParam queryParam = (QueryParam) knownAnnotation;
                    String parameterName = queryParam.value();
                    if (!queryStringMap.containsKey(parameterName))
                        throw new RuntimeException(parameterName + " not found in query string"); // TODO
                    String parameterValue = queryStringMap.get(parameterName);
                    methodParameterInstance[i] = toObjectOfParameterType(parameterValue, method, i, queryParam.format());
                } else if (knownAnnotation instanceof RequestHeader) {
                    RequestHeader requestHeader = (RequestHeader) knownAnnotation;
                    String parameterName = requestHeader.value();
                    if (!headerMap.containsKey(parameterName))
                        throw new RuntimeException(parameterName + " not found in request header"); // TODO
                    String parameterValue = headerMap.get(parameterName);
                    methodParameterInstance[i] = toObjectOfParameterType(parameterValue, method, i, requestHeader.format());
                }
            }
        }
        return methodParameterInstance ;
    }

    /**
     *
     * @param value
     * @param method
     * @param parameterIndex
     * @param format
     * @return
     * @throws IOException
     */
    private Object toObjectOfParameterType(String value, Method method, int parameterIndex, String format) throws IOException {
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        if (isPrimaryType(parameterType)) {
            return instanceForPrimaryValue(value, parameterType, format);
        } else {
            return instanceForComplexType(value, method, parameterType, parameterIndex) ;
        }
    }

    /**
     *
     * @param value
     * @param method
     * @param parameterType
     * @param parameterIndex
     * @return
     * @throws IOException
     */
    private Object instanceForComplexType(String value, Method method, Class<?> parameterType, int parameterIndex) throws IOException {
        if ( value == null )
            return null ;
        JavaType type = null ;
        if (Collection.class.isAssignableFrom(parameterType)) {
            Class<? extends Collection<?>> listType = (Class<? extends Collection<?>>)parameterType ;
            Type genericType = method.getGenericParameterTypes()[parameterIndex] ;
            Class<?> listParamClass = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
            type = jacksonObjectMapper.getTypeFactory().constructCollectionType(listType, listParamClass);
        } else {
            type = jacksonObjectMapper.getTypeFactory().constructType(parameterType) ;
        }
        Object paramObject = jacksonObjectMapper.readValue(value, type) ;
        return paramObject ;
    }

    private boolean isPrimaryType(Class<?> clazz) {
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) || clazz.equals(int.class) ||
                clazz.equals(Long.class) || clazz.equals(long.class) ||
                clazz.equals(Float.class) || clazz.equals(float.class) ||
                clazz.equals(Double.class) || clazz.equals(double.class) ||
                clazz.equals(Boolean.class) || clazz.equals(boolean.class) ||
                clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) ;
    }

    private <U> U instanceForPrimaryValue(String value, Class<U> clazz, String format) {
        if (value == null )
            return null ;

        if (clazz.equals(String.class)) {
            return (U) value;
        }

        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return (U) Integer.valueOf(value);
        }

        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return (U) Long.valueOf(value);
        }

        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return (U) Boolean.valueOf(value);
        }

        if (clazz.equals(Date.class)) {
            DateFormat dateFormat = getDateFormat(format);
            try {
                return (U) dateFormat.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException("Cannot parse " + value);
            }
        }

        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            try {
                Number number = getNumberFromString(value);
                return (U) Float.valueOf(number.floatValue());
            } catch (ParseException e) {
                throw new RuntimeException("Cannot parse " + value);
            }
        }

        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            try {
                Number number = getNumberFromString(value);
                return (U) Double.valueOf(number.floatValue());
            } catch (ParseException e) {
                throw new RuntimeException("Cannot parse " + value);
            }
        }

        if (clazz.equals(BigDecimal.class)) {
            DecimalFormat decimalFormat = getDecimalFormat(format);
            try {
                return (U) decimalFormat.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException("Cannot parse " + value);
            }
        }

        return (U) value;
    }

    private DateFormat getDateFormat(String format) {
        if (format == null || format.isEmpty()) {
            return DateFormat.getDateInstance();
        } else {
            return new SimpleDateFormat(format);
        }
    }

    private DecimalFormat getDecimalFormat(String format) {
        DecimalFormat instance;
        if ( format == null ||  format.isEmpty()) {
            instance = (DecimalFormat) DecimalFormat.getInstance();
        } else {
            instance = new DecimalFormat(format);
        }
        instance.setParseBigDecimal(true);
        return instance;
    }

    private Number getNumberFromString(String value) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.parse(value);
    }
}
