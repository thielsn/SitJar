/*
 *
 * Container for handling SQL results
 * @version $Revision: $
 */
package sit.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author thiel
 */
public class SQLResultContainer {

    private ResultSet resultSet = null;
    private Statement statement = null;

    public SQLResultContainer(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    public void close() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (statement != null) {
            statement.close();
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public Statement getStatement() {
        return statement;
    }
}
