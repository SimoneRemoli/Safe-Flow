package it.web.routex.utility.builder;

import it.web.routex.boundary.cli.view.PathNOREGCLI;
import it.web.routex.utility.builder.data.PathInit;

public class PathNORegInitBuilder
{
    private String status;
    private String start;
    private String end;
    private String city;

    public PathNORegInitBuilder(String status)
    {
        this.status = status;
    }
    public PathNORegInitBuilder start(String start)
    {
        this.start = start;
        return this;
    }
    public PathNORegInitBuilder end(String end)
    {
        this.end = end;
        return this;
    }
    public PathNORegInitBuilder city(String city)
    {
        this.city = city;
        return this;
    }
    public void build() {
        PathInit data = new PathInit();
        data.setCity(city);
        data.setEnd(end);
        data.setStatus(status);
        data.setStart(start);
        PathNOREGCLI.from2(data);
    }
}
