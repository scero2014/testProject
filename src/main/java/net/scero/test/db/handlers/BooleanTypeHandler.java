package net.scero.test.db.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.BaseTypeHandler;

/**
 * Example with values S,N to boolean Handler. Compatibility with 'Y' value
 *
 */
@MappedTypes({
    Boolean.class, boolean.class
})
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {
    private static final String FALSE_STRING = "N";
    private static final String TRUE_STRING = "Y";
    private static final String TRUE_ALTERNATIVE_STRING = "S";

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int index, final Boolean parameter, final JdbcType jdbcType)
        throws SQLException {
        if (parameter != null) {
            ps.setString(index, parameter.booleanValue() ? TRUE_STRING : FALSE_STRING);
        } else {
            ps.setString(index, FALSE_STRING);
        }
    }

    @Override
    public Boolean getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return TRUE_STRING.equalsIgnoreCase(rs.getString(columnName)) || TRUE_ALTERNATIVE_STRING.equalsIgnoreCase(rs.getString(columnName));
    }

    @Override
    public Boolean getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return TRUE_STRING.equalsIgnoreCase(rs.getString(columnIndex)) || TRUE_ALTERNATIVE_STRING.equalsIgnoreCase(rs.getString(columnIndex));
    }

    @Override
    public Boolean getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return TRUE_STRING.equalsIgnoreCase(cs.getString(columnIndex)) || TRUE_ALTERNATIVE_STRING.equalsIgnoreCase(cs.getString(columnIndex));
    }
}
