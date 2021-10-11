package org.malacca.component.procedure;

import org.malacca.support.helper.FreeMarker;

import java.math.BigDecimal;

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
public class DefaultValueHandler implements ValueHandler {

    private FreeMarker freeMarker;

    public DefaultValueHandler() {
    }

    public DefaultValueHandler(FreeMarker freeMarker) {
        this.freeMarker = freeMarker;
    }

    @Override
    public Object handle(ProcedureParam param) {
        try {
            String tempValue = freeMarker.parseExpression(param.getValue());
            if (DataType.INT.name().equalsIgnoreCase(param.getDataType())) {
                return Integer.parseInt(tempValue);
            } else if (DataType.NUMBER.name().equalsIgnoreCase(param.getDataType())) {
                return new BigDecimal(param.getValue());
            }
            return freeMarker.parseExpressionExtend(tempValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FreeMarker getFreeMarker() {
        return freeMarker;
    }

    public void setFreeMarker(FreeMarker freeMarker) {
        this.freeMarker = freeMarker;
    }

}

