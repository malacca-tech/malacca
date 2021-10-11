package org.malacca.component.procedure;

import org.malacca.support.helper.FreeMarker;
import org.malacca.utils.DatasourceUtils;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.malacca.component.procedure.ProcedureParam.ProcedureParamMode;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
public class ProcedureCallableStatementCreator implements CallableStatementCreator {

    private List<ProcedureParam> paramList = new ArrayList<>();

    private FreeMarker freeMarker;

    private DataSource dataSource;

    private String procSql;

    private ValueHandler valueHandler;

    public ProcedureCallableStatementCreator() {
    }

    public ProcedureCallableStatementCreator(List<ProcedureParam> paramList, DataSource dataSource, String procSql, ValueHandler valueHandler) {
        this.paramList = paramList;
        this.dataSource = dataSource;
        this.procSql = procSql;
        this.valueHandler = valueHandler;
    }

    @Override
    public CallableStatement createCallableStatement(Connection connection) throws SQLException {
        //给SQL设置参数
        CallableStatement cs = connection.prepareCall(procSql);
        if (DatasourceUtils.isOracle(dataSource)
                || DatasourceUtils.isSqlserver(dataSource)
                || DatasourceUtils.isMysql(dataSource)
                || DatasourceUtils.isPostgresql(dataSource)) {
            for (int i = 0; i < paramList.size(); i++) {
                ProcedureParam param = paramList.get(i);
                int position = param.getPosition() > 0 ? param.getPosition() : i + 1;
                if (ProcedureParamMode.IN.name().equals(param.getMode())) {
                    Object value = valueHandler.handle(param);
                    cs.setObject(position, value);
                } else if (ProcedureParamMode.OUT.name().equals(param.getMode())
                        || ProcedureParamMode.INOUT.name().equals(param.getMode())) {
                    cs.registerOutParameter(position, DatasourceUtils.dataType2code(param.getDataType()));
                }
            }
        } else {
            throw new RuntimeException("暂不支持该数据库类型" + dataSource.toString());
        }
        return cs;
    }

    public ValueHandler getValueHandler() {
        return valueHandler;
    }

    public void setValueHandler(ValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    public FreeMarker getFreeMarker() {
        return freeMarker;
    }

    public void setFreeMarker(FreeMarker freeMarker) {
        this.freeMarker = freeMarker;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getProcSql() {
        return procSql;
    }

    public void setProcSql(String procSql) {
        this.procSql = procSql;
    }

    public List<ProcedureParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ProcedureParam> paramList) {
        this.paramList = paramList;
    }
}

