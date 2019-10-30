package nl.azwaan.quotedb.api.filters;

import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

import java.util.List;

public class LabelsFilterBuilder extends BaseFilterBuilder<Label> {

    /**
     * Constructs a new basic filter builder for labels.
     * Filters for owning user, and filters out deleted labels by default.
     * @param dao The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request The request to build a filter for.
     */
    public LabelsFilterBuilder(BaseDAO<Label> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected List<Filter> getFilters(Request request) {
        final List<Filter> res = super.getFilters(request);
        // Add filter for label name.
        mapStringLikeFilter(request, res, "labelName", Label.LABEL_NAME);
        return res;
    }
}
