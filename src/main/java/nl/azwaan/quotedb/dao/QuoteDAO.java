package nl.azwaan.quotedb.dao;

import nl.azwaan.quotedb.models.Quote;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * Database access for pets.
 */
@RegisterRowMapper(Quote.Mapper.class)
public interface QuoteDAO {
  /**
   * List pets using start/max limits.
   *
   * @param start Start offset.
   * @param max Max number of rows.
   * @return Available pets.
   */
  @SqlQuery("select * from pets limit :max offset :start")
  List<Quote> list(int start, int max);

  /**
   * Find a pet by ID.
   *
   * @param id Quote ID.
   * @return Quote or null.
   */
  @SqlQuery("select * from pets where id=:id")
  Quote findById(long id);

  /**
   * Insert a quote and returns the generated PK.
   *
   * @param quote Quote to insert.
   * @return Primary key.
   */
  @SqlUpdate("insert into pets(author) values(:author)")
  @GetGeneratedKeys
  long insert(@BindBean Quote quote);

  /**
   * Update a quote and returns true if the pets was updated.
   *
   * @param quote Quote to update.
   * @return True if the quote was updated.
   */
  @SqlUpdate("update pets set author=:author where id=:id")
  boolean update(@BindBean Quote quote);

  /**
   * Delete a pet by ID.
   *
   * @param id Quote ID.
   * @return True if the pet was deleted.
   */
  @SqlUpdate("delete pets where id=:id")
  boolean delete(long id);
}
