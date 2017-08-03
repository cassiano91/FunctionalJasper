package functionaljasper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.JRException;

public class Reports {

    @SuppressWarnings("rawtypes")
    public static String javaDir(Class aClass) throws IOException {
        String dir = aClass.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        dir = dir.substring(6, dir.length() - aClass.getName().length() + 1) + "/";

        return dir.replaceAll("%20", " ");
    }

    @SuppressWarnings("rawtypes")
    public static String webDir(Class aClass) {
        String dir = aClass.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        dir = dir.substring(6, dir.length() - aClass.getName().length() - 23) + "/";
        return dir.replaceAll("%20", " ");
    }

    public static String loadFile(String fileName) throws IOException {
        File file                  = new File(fileName);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner            = new Scanner(file);
        String lineSeparator       = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    public static void removeFile(String fileName) {
        try {
            File file = new File(webDir(Reports.class) + fileName);
            file.delete();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
}

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }

        return String.valueOf(chars);
    }

    public String generateHtmlReport(String jasperFile, List<Object> list, Map<String, Object> param) throws Exception {
        String data = String.valueOf(Calendar.getInstance().getTime().getTime());
        String url = "cassiano.melo/workspace/temp/" + data + ".html";
        jasperFile = javaDir(Reports.class) + "plsweb/backend/test/br/com/unimeduberaba/reports/" + jasperFile + ".jasper";

        try {
            JasperRunManager.runReportToHtmlFile(jasperFile, webDir(Reports.class) + url, param, new JRBeanCollectionDataSource(list));
        } catch (JRException e) {
            System.err.println(e.getMessage());
            if (e.getCause().toString().contains("FileNotFoundException")) {
                throw new Exception("Erro ao gerar o relatório: arquivo compilado não encontrado.");
            }

            throw new Exception("Erro ao gerar o relatório.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return url;
    }

    public byte[] generatePdfReport(String jasperFile, List<Object> list, Map<String, Object> param)  throws Exception {
        byte[] bytes = null;

        jasperFile = javaDir(Reports.class) + "alJasper/src/functionaljasper/reports/" + jasperFile + ".jasper";

        try {
            bytes = JasperRunManager.runReportToPdf(jasperFile, param, new JRBeanCollectionDataSource(list));
        } catch (JRException e) {
            System.err.println(e.getMessage());

            if (e.getCause().toString().contains("FileNotFoundException")) {
                throw new Exception("Erro ao gerar o Pdf do relatório: arquivo compilado não encontrado.");
            }

            throw new Exception("Erro ao gerar o Pdf do relatório.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return bytes;
    }

    public byte[] generateXlsReport(List<Object> list, Map<String, Object> param, String jasperFile) throws Exception {
        jasperFile = javaDir(Reports.class) + "plsweb/backend/test/br/com/unimeduberaba/reports/" + jasperFile + ".jasper";

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperFile, param, dataSource);
        } catch (JRException e) {
            System.err.println(e.getMessage());

            if (e.getCause().toString().contains("FileNotFoundException")) {
                throw new Exception("Erro ao gerar o Xls do relatório: arquivo compilado não encontrado.");
            }

            throw new Exception("Erro ao gerar o XLS do relatório.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        JRXlsExporter export = new JRXlsExporter();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        export.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        export.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
        export.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        export.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        export.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, Integer.decode("65000"));
        export.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        export.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        export.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

        try {
            export.exportReport();
        } catch (JRException e) {
            System.err.println(e.getMessage());
        }

        byte[] bytes = output.toByteArray();

        return bytes;
    }
}