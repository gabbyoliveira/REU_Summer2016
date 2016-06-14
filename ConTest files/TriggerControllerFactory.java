package conTest;

public class TriggerControllerFactory
{
    public static TriggerController createTriggerController(String className)
    {
        try
        {
            Class<?> cls = Class.forName(className);
            Object result = cls.newInstance();
            if (result instanceof TriggerController)
                return (TriggerController) result;
            throw new RuntimeException("Cannot create trigger controller of type: " + className
                                       + ", received: " + result.getClass());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Cannot create trigger controller.", e);
        }
    }
}
