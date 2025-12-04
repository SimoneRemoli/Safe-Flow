package it.web.routex.utility.decorator.decoratorchange;

import java.util.List;

public interface Component
{
    List<String> getChanges(List<String> changes);
}
