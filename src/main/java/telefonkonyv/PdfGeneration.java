package telefonkonyv;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.collections.ObservableList;

/**
 * PDF létrehozása
 */
public class PdfGeneration {

    /**
     * PDF fálj készítése a kontaktok elmentéséhez
     * @param fajlnev a fájl kívánt neve
     * @param adat a felvett személyek listája
     */
    public void pdfGeneration(String fajlnev, ObservableList<Person> adat) {
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fajlnev + ".pdf"));
            document.open();
            
            float[] columnWidths = {2, 4, 4, 6};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("Kontaktok"));
            cell.setBackgroundColor(GrayColor.GRAYWHITE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);
            
            table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("Sorszám");
            table.addCell("Vezetéknév");
            table.addCell("Keresztnév");
            table.addCell("Telefonszám");
            
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
            for (int counter = 1; counter <= adat.size(); counter++) {
                Person actualPerson = adat.get(counter - 1);
                
                table.addCell(""+counter);
                table.addCell(actualPerson.getLastName());
                table.addCell(actualPerson.getFirstName());
                table.addCell(actualPerson.getPhoneNumber());
            }
              
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }
}
