package org.malacca.component.procedure;

import oracle.jdbc.OracleTypes;
import org.malacca.utils.DatasourceUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
public class ProcedureCallableStatementCallback implements CallableStatementCallback {

    private DataSource dataSource;

    private List<ProcedureParam> paramList = new ArrayList<>();

    private ResultSetExtractor resultSetExtractor;

    public ProcedureCallableStatementCallback() {
    }

    public ProcedureCallableStatementCallback(DataSource dataSource, List<ProcedureParam> paramList, ResultSetExtractor resultSetExtractor) {
        this.dataSource = dataSource;
        this.paramList = paramList;
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
        callableStatement.execute();
        ResultSet resultSet = null;
        if (DatasourceUtils.isOracle(dataSource)
                || DatasourceUtils.isMysql(dataSource)) {
            for (int i = 0; i < paramList.size(); i++) {
                ProcedureParam param = paramList.get(i);
                int position = param.getPosition() > 0 ? param.getPosition() : i + 1;
                if ("OUT".equals(param.getMode())) {
                    if (OracleTypes.CURSOR == DatasourceUtils.dataType2code(param.getDataType())) {
                        Object result = callableStatement.getObject(position);
                        if (result instanceof ResultSet) {
                            resultSet = (ResultSet) result;
                        }
                    } else {
                        return callableStatement.getObject(position);
                    }
                }
            }
            return "";
        } else if (DatasourceUtils.isSqlserver(dataSource)) {
            resultSet = callableStatement.getResultSet();
        } else {
            throw new RuntimeException("暂不支持该数据库类型" + dataSource.toString());
        }
        if (null == resultSet) {
            return "";
        }
        return resultSetExtractor.extractData(resultSet);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResultSetExtractor getResultSetExtractor() {
        return resultSetExtractor;
    }

    public void setResultSetExtractor(ResultSetExtractor resultSetExtractor) {
        this.resultSetExtractor = resultSetExtractor;
    }

    public List<ProcedureParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ProcedureParam> paramList) {
        this.paramList = paramList;
    }
}

