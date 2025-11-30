package Model.Extractor;

import Model.Record.BuyTicketRecord;

import javax.servlet.http.HttpServletRequest;

public class BuyTicketExtractor
{
    public static BuyTicketRecord from (HttpServletRequest request)
    {
        String city = request.getParameter("city");
        String quantity = request.getParameter("quantity");
        return new BuyTicketRecord(
            city,
            Integer.parseInt(quantity)
        );
    }
}
