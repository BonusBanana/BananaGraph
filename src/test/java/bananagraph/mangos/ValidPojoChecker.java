package bananagraph.mangos;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ValidPojoChecker {


    public static boolean checkPojo(Constructor<?> constructor) {

        try {

            Object instance = constructor.newInstance();


            Map<String, List<Method>> methodsMap =
                    Arrays.stream(constructor.getDeclaringClass().getDeclaredMethods())
                            .collect(Collectors.groupingBy(m -> m.getName().substring(0, 3)));

            // only getters and setter
            assertEquals(2, methodsMap.size());
            assertTrue(methodsMap.containsKey("get"));
            assertTrue(methodsMap.containsKey("set"));


            // set and get every setter and getter
            for (Method setter : methodsMap.get("set")) {

                Class<?> param = setter.getParameterTypes()[0];

                Object setValue = null;

                if (param.isPrimitive()) {

                    switch (param.getName()) {
                        case "boolean":
                            setValue = true;
                            break;
                        case "byte":
                            setValue = (byte) 1;
                            break;
                        case "char":
                            setValue = 's';
                            break;
                        case "short":
                            setValue = (short) 2;
                            break;
                        case "int":
                            setValue = 3;
                            break;
                        case "long":
                            setValue = 4L;
                            break;
                        case "float":
                            setValue = 5.1f;
                            break;
                        case "double":
                            setValue = 6.2d;
                            break;
                    }

                }

                setter.invoke(instance, setValue);

                String getterName = "get" + setter.getName().substring(3);
                Method getter = methodsMap.get("get").stream()
                        .filter(m -> getterName.equals(m.getName()))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException("no matching getter"));

                Object getValue = getter.invoke(instance);

                assertEquals(setValue, getValue);

            }


        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
