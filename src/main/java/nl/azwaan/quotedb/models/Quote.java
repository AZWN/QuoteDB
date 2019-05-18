package nl.azwaan.quotedb.models;

import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Quote {

    public static class Mapper implements RowMapper<Quote> {
        @Override
        public Quote map(final ResultSet rs, final StatementContext ctx) throws SQLException {
            return new Quote(rs.getLong("id"),
                rs.getString("text"),
                rs.getString("author"),
                rs.getString("source"));
        }
    }

    /**
     * Quote id in database
     */
    @NonNull private Long id;

    /**
     * Quote text
     */
    @NonNull private String text;

    /**
     * Quote author
     */
    @NonNull private String author;

    /**
     * Quote source
     */
    @NonNull private String source;
}
