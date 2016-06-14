package analysis;

import org.eclipse.jdt.annotation.Nullable;

public class TimelessLogEvent
{
    private final String action_;
    private final String information_;

    public TimelessLogEvent(String action, String information)
    {
        action_ = action;
        information_ = information;
    }

    @Override
    public int hashCode()
    {
        int result = action_.hashCode();
        result = result * 17 + information_.hashCode();
        return result;
    }

    @Override
    public boolean equals(@Nullable Object other)
    {
        if (other instanceof TimelessLogEvent)
            return equals((TimelessLogEvent) other);
        return false;
    }

//    public boolean equals(TimelessLogEvent other)
//    {
//        return action_.equals(other.action_) && information_.equals(other.information_);
//    }

    public boolean equals(TimelessLogEvent other)
    {
        return action_.equals(other.action_)
               && areInformationsSimilar(information_, other.information_);
    }

    private static boolean areInformationsSimilar(String information1, String information2)
    {
        if (information1.length() != information2.length())
            return false;

        int diffCount = 0;
        for (int a = 0; a < information1.length(); a++)
        {
            char char1 = information1.charAt(a);
            char char2 = information2.charAt(a);
            if (char1 != char2)
                diffCount++;
        }
        return diffCount <= 1;
    }

    @Override
    public String toString()
    {
        return "[Event: action = " + action_ + ", information = " + information_ + "]";
    }
}
