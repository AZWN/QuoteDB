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

public abstract class BaseAPI {

    protected <T extends Persistable> MultiResultPage<T> getPagedResult(Request request, BaseDAO<T> dao,
            Function<Selection, Selection> queryFilterBuilder)
    {
        final int pageSize = request.param("page").intValue(Constants.MAX_PAGE_SIZE);
        final int pageNumber = request.param("pageNumber").intValue(1);
        final int totalResults = ((Scalar<Integer>) queryFilterBuilder.apply(dao.countQuery())).value();

        final List<T> data = new ArrayList<>(pageSize);

        ((Result<T>) queryFilterBuilder.apply(dao.selectQuery()).get())
                .iterator((pageNumber - 1) * pageSize, pageSize)
                .forEachRemaining(data::add);

        final MultiResultPage<T> resultPage =
                MultiResultPage.resultPageFor(data, totalResults, pageSize, pageNumber, request.path());

        return resultPage;
    }
}
