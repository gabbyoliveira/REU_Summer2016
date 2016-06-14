package utility;

import org.gibello.zql.ZFromItem;
import org.gibello.zql.ZSelectItem;

public class ZQLDebug
{
    public static String toString(ZSelectItem select)
    {
        String aggregate = select.getAggregate();
        String alias = select.getAlias();
        String column = select.getColumn();
        String schema = select.getSchema();
        String table = select.getTable();

        return "[ZSelectItem: aggregate = " + aggregate + ", alias = " + alias + ", column = "
               + column + ", schema = " + schema + ", table = " + table + "]";
    }

    public static String toString(ZFromItem from)
    {
        String alias = from.getAlias();
        String column = from.getColumn();
        String schema = from.getSchema();
        String table = from.getTable();

        return "[ZFromItem: alias = " + alias + ", column = " + column + ", schema = " + schema
               + ", table = " + table + "]";
    }
}
