package testing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import conTest.CDTBase;
import conTest.SharedConstants;
import conTest.SharedOperations;
import database.DBOperations;

public class Test implements SharedConstants, Runnable
{
    private final static MessageDigest digest_;
    static
    {
        try
        {
            // Suppressed due to missing library annotations.
            @SuppressWarnings("null") @NonNull MessageDigest digest =
                MessageDigest.getInstance("SHA-1");
            // Note that the maximum permitted characters for a table name or trigger name is 256
            // characters, that is why with 'cdt' prefix, SHA-256 hashing is too long. We can return
            // back to SHA-256 and trim last 3 characters alternatively.
            digest_ = digest;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> ids_ = new ArrayList<>();

    public static final Test NULL = new Test();

    private final String id_;
    private final String testQuery_;

    private @Nullable PreparedStatement diffQueryStatement1_;
    private @Nullable PreparedStatement diffQueryStatement2_;
    private @Nullable PreparedStatement countQueryStatement1_;
    private @Nullable PreparedStatement countQueryStatement2_;

    private Test()
    {
        this("");
    }

    public String getTestQuery()
    {
        return testQuery_;
    }

    public Test(String testQuery)
    {
        testQuery_ = testQuery;
        id_ = computeID(testQuery);

        diffQueryStatement1_ = null;
        diffQueryStatement2_ = null;
        countQueryStatement1_ = null;
        countQueryStatement2_ = null;
    }

    protected boolean shallCreateTriggers()
    {
        return true;
    }

    public Runnable createInitializationJob()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    initializeTempTable();
                    compileTestQueries();

                    if (shallCreateTriggers())
                        CDTBase.getInstance().createTriggersForQuery(getFinalID(), getTestQuery());

                    CDTBase.getInstance().testInitialized(Test.this);
                }
                catch (Exception e)
                {
                    CDTBase.getInstance().testInitialized(Test.this, e);
                }
            }
        };
    }

    protected void initializeTempTable() throws SQLException
    {
        DBOperations.createTable(getFinalID(), getTestQuery());
    }

    void compileTestQueries() throws SQLException
    {
        String diffQuery1 = "(SELECT * FROM " + getFinalID() + ") EXCEPT (" + testQuery_ + ")";
        String diffQuery2 = "(" + testQuery_ + ") EXCEPT (SELECT * FROM " + getFinalID() + ")";
        String countQuery1 = "SELECT COUNT(*) FROM " + getFinalID();
        String countQuery2 = "SELECT COUNT(*) FROM (" + testQuery_ + ") as temp";

        diffQueryStatement1_ = DBOperations.prepareStatement(diffQuery1);
        diffQueryStatement2_ = DBOperations.prepareStatement(diffQuery2);
        countQueryStatement1_ = DBOperations.prepareStatement(countQuery1);
        countQueryStatement2_ = DBOperations.prepareStatement(countQuery2);
    }

    public String getFinalID()
    {
        return CDT_PREFIX + id_;
    }

    public String[] getFinalIDs()
    {
        return new String[] {getFinalID()};
    }

    /**
     * Returns true is the test fails.
     * 
     * @return
     * @throws SQLException
     */
    @Override
    public void run()
    {
        runInternal();
    }

    // Note that this method should be synchronized (as recacheResults(..)) since we don't want both
    // running at the same time in different threads.
    private synchronized void runInternal()
    {
        PreparedStatement diffQueryStatement1 = diffQueryStatement1_;
        PreparedStatement diffQueryStatement2 = diffQueryStatement2_;
        PreparedStatement countQueryStatement1 = countQueryStatement1_;
        PreparedStatement countQueryStatement2 = countQueryStatement2_;

        if (diffQueryStatement1 == null || diffQueryStatement2 == null
            || countQueryStatement1 == null || countQueryStatement2 == null)
        {
            System.out.println(this + " is not compiled, not running test...");
            // For some reason test has not been compiled, return.
            return;
        }

        CDTBase.getInstance().testStarted(this);
        try
        {
            boolean result = true;

            try (ResultSet count1 = countQueryStatement1.executeQuery())
            {
                count1.next();
                int existingCount = count1.getInt(1);
                try (ResultSet count2 = countQueryStatement2.executeQuery())
                {
                    count2.next();
                    int currentCount = count2.getInt(1);
                    result = (existingCount == currentCount);
                }
            }

            if (result)
            {
                try (ResultSet diff1 = diffQueryStatement1.executeQuery())
                {
                    result = !diff1.next();
                }
            }

            if (result)
            {
                try (ResultSet diff2 = diffQueryStatement2.executeQuery())
                {
                    result = !diff2.next();
                }
            }

            if (result)
                CDTBase.getInstance().testCompleted(this, TestResult.PASS);
            else
                CDTBase.getInstance().testCompleted(this, TestResult.FAIL);
        }
        catch (SQLException e)
        {
            CDTBase.getInstance().testCompleted(this, TestResult.ERROR, e);
        }
    }

    public void removeFromDB() throws SQLException
    {
        DBOperations.dropTable(getFinalID());
    }

    @Override
    public boolean equals(@Nullable Object object)
    {
        if (object instanceof Test)
            return equals((Test) object);
        return false;
    }

    @Override
    public int hashCode()
    {
        return id_.hashCode();
    }

    public boolean equals(Test other)
    {
        return id_.equals(other.id_);
    }


    @Override
    public String toString()
    {
        return "[Test: id = " + id_ + ", query = " + SharedOperations.toOneLine(testQuery_) + "]";
    }

    // Note that this method should be synchronized (as runInternal(..)) since we don't want both
    // running at the same time in different threads.
    public synchronized void recacheResults() throws SQLException
    {
        DBOperations.dropTable(getFinalID());
        initializeTempTable();
    }

    private static String computeID(String identifier)
    {
        digest_.reset();
        digest_.update(identifier.getBytes());

        byte[] digestResult = digest_.digest();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < digestResult.length; i++)
            result.append(Integer.toString((digestResult[i] & 0xff) + 0x100, 16).substring(1));

        // Suppressed due to missing library annotations.
        @SuppressWarnings("null") @NonNull String res = result.toString();

        String temp = res;
        int counter = 1;
        while (ids_.contains(temp))
        {
            counter++;
            temp = res + "_" + counter;
        }

        ids_.add(temp);
        return temp;
    }

    public List<Test> getParts()
    {
        ArrayList<Test> result = new ArrayList<>();
        result.add(this);
        return result;
    }
}
