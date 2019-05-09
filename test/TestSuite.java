import it.reply.NGUM_Inbound.DWHReconManager;
import it.reply.NGUM_Inbound.DWHTaskSupport;
import org.junit.Test;
import Exception.*;

import java.util.HashMap;

public class TestSuite {

    @Test
    public void testAccount_Test() throws DWHargetReconManagerException {
        DWHReconManager reconManager = new DWHReconManager(
                "20190507_accounts_DWH.csv",
                true,
                "ADONIS_TEST",
                "Lookup.ADONIS.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

    @Test
    public void testTaskSupportAccount() throws Exception {
        HashMap<String,Object> attrs = new HashMap<>();
        attrs.put("File Path","20190507_accounts_DWH.csv");
        attrs.put("AppInstanceName","ADONIS_TEST");
        attrs.put("Lookup","Lookup.ADONIS.Roles");
        attrs.put("Batch Size","100");

        new DWHTaskSupport().execute(attrs);
    }

}
