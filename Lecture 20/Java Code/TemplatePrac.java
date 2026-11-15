/*
Report Processing System
Build a report processing application supporting:
- PDF Reports
- CSV Reports
Both reports must follow the same overall workflow:
1. Validate input data.
2. Fetch the data.
3. Transform the data.
4. Generate the report.
5. Save the report.
Requirements:
- The overall sequence must remain the same.
- PDF and CSV should implement their respective report-generation logic.
- Some steps can have a common implementation.
- Some steps can have different implementations.
- A subclass should not be able to change the overall workflow.
*/

abstract class ReportGenerator{

    // template method defining the overall workflow
    public final void generateReport(String data)
    {
        String finalData = fetchData(data);
        validateData(finalData);
        transformData(finalData);
        generateReportFile(finalData);
        saveReport(finalData);
    }
    protected void validateData(String data)
    {
        System.out.println("Validating data: " + data);
    }

    protected String fetchData(String data)
    {
        System.out.println("Fetching data: " + data);
        return "Data from File Path " + data;
    }

    protected abstract void transformData(String data);
    protected abstract void generateReportFile(String data);

    protected void saveReport(String data)
    {
        System.out.println("Saving report: " + data);
    }
}

class PDFReportGenerator extends ReportGenerator{
    @Override
    protected void transformData(String data) {
        System.out.println("Transforming data for PDF: " + data);
    }

    @Override
    protected void generateReportFile(String data) {
        System.out.println("Generating PDF report: " + data);
    }
}

class CSVReportGenerator extends ReportGenerator{
    @Override
    protected void transformData(String data) {
        System.out.println("Transforming data for CSV: " + data);
    }

    @Override
    protected void generateReportFile(String data) {
        System.out.println("Generating CSV report: " + data);
    }
}

public class TemplatePrac {
    public static void main(String[] args) {
        ReportGenerator pdfReport = new PDFReportGenerator();
        pdfReport.generateReport("path/to/pdf/data");

        System.out.println();

        ReportGenerator csvReport = new CSVReportGenerator();
        csvReport.generateReport("path/to/csv/data");
    }
}
