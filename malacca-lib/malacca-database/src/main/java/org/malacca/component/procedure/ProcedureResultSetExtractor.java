package org.malacca.component.procedure;

import org.malacca.utils.SimpleJson;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/7/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class ProcedureResultSetExtractor implements ResultSetExtractor {

    @Override
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        return SimpleJson.buildSuccessJsonRecords(rs);
    }

}

