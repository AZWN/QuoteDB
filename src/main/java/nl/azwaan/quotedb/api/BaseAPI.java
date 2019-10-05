package nl.azwaan.quotedb.api;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Selection;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.BaseDAO;
import org.jooby.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

public abstract class BaseAPI {

    protected <T extends Persistable> MultiResultPage<T> getPagedResult(Request request, BaseDAO<T> dao,
            Function<Selection, Selection> queryFilterBuilder)
    {
        final int pageSize = request.param("pageSize").intValue(Constants.MAX_PAGE_SIZE);
        final int pageNumber = request.param("page").intValue(1);

        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100 (inclusive)");
        }

        if (pageNumber < 0) {
            throw new IllegalArgumentException("pageNumber must be 0 or more");
        }

        final int totalResults = ((Scalar<Integer>) queryFilterBuilder.apply(dao.countQuery()).get()).value();

        final List<T> data = new ArrayList<>(pageSize);

        ((Result<T>) queryFilterBuilder.apply(dao.selectQuery()).get())
                .iterator((pageNumber - 1) * pageSize, pageSize)
                .forEachRemaining(data::add);

        final MultiResultPage<T> resultPage =
                MultiResultPage.resultPageFor(data, totalResults, pageSize, pageNumber, request.path());

        return resultPage;
    }
}
