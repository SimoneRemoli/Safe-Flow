package Model.Extractor;

import Model.Domain.TypesOfPersistenceLayer;
import Model.Record.PaymentRecord;

import javax.servlet.http.HttpServletRequest;

public class PagamentoExtractor
{
    public static PaymentRecord from (HttpServletRequest request)
    {
        String city = request.getParameter("city");
        String quantityParam = request.getParameter("quantity");
        String totaleParam = request.getParameter("totale");
        String metodo = request.getParameter("metodoPagamento");
        String persistence = request.getParameter("persistence");

        return new PaymentRecord(
            city,
            Integer.parseInt(quantityParam),
            Double.parseDouble(totaleParam),
            metodo,
            persistence.equals("JDBC") ? TypesOfPersistenceLayer.JDBC : TypesOfPersistenceLayer.FileSystem
        );
    }
}
