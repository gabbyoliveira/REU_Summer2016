package conTest;

public class TestControllerFactory
{
    public static TestController createTestController(String className)
    {
        try
        {
            Class<?> cls = Class.forName(className);
            Object result = cls.newInstance();
            if (result instanceof TestController)
                return (TestController) result;
            throw new RuntimeException("Cannot create test controller of type: " + className
                                       + ", received: " + result.getClass());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Cannot create test controller.", e);
        }
    }
}
