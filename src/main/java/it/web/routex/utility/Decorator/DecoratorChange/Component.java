package it.web.routex.utility.Decorator.DecoratorChange;

import java.util.ArrayList;
import java.util.List;

public interface Component
{
    List<String> getChanges(List<String> changes);
}
