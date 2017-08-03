package functionaljasper;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

public class FunctionalJasper {

    public static void main(String[] args) throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, Object> param = new HashedMap();

        param.put("arquivoJasper", new Date().toString());
        List<Object> qualificacoes = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            CodigoNome cn = new CodigoNome(""+i, "CodigoNome " + i);

            qualificacoes.add(cn);
        }

        byte[] result = new Reports().generatePdfReport("teste_iReport", qualificacoes, param);

        String data = String.valueOf(Calendar.getInstance().getTime().getTime());
        String url = Reports.javaDir(Reports.class) + "/temp/" + data;

        OutputStream out = new FileOutputStream(url + ".pdf");
        out.write(result);
        out.close();        
    }
}
