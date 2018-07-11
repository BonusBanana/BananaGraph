package bananagraph.mangos;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;

import static junit.framework.TestCase.assertTrue;

public class CheckAllClassesArePojos {


    @Test
    public void testAll() {


        Reflections reflections = new Reflections("bananagraph.mangos",
                new SubTypesScanner(false),
                new MethodParameterScanner());

        reflections.getConstructorsMatchParams().stream()
                .filter(c -> !this.getClass().equals(c.getDeclaringClass()))
                .filter(c -> !ValidPojoChecker.class.equals(c.getDeclaringClass()))
                .forEach(c -> assertTrue(ValidPojoChecker.checkPojo(c)));
    }

}
