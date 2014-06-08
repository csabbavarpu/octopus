package framework.annotation;


import java.lang.annotation.Annotation;

import org.testng.ITestResult;

/**
 * Contains some utility methods for testNG annotated methods.
 */
public class AnnotationHelper {
    /**
     * Checks if the given method exists on the current ITestResult method or its parent class.
     */
    public static <T extends Annotation> boolean hasAnnotation(ITestResult result, Class<T> a) {
        return result.getMethod().getMethod().getAnnotation(a) != null ||
               result.getTestClass().getRealClass().getAnnotation(a) != null;
    }

    /**
     * Checks if the given annotation exists on the current ITestResult method or its parent class.
     */
    public static <T extends Annotation> T getAnnotation(ITestResult result, Class<T> a) {
        T ano = result.getMethod().getMethod().getAnnotation(a);;
        if (ano != null) {
            return ano;
        }

        ano = (T) result.getTestClass().getRealClass().getAnnotation(a);

        if (ano == null) {
            throw new NullPointerException("Annotation not found");
        }

        return ano;
    }
}
