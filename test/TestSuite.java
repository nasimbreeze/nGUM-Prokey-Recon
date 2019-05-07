import it.reply.NGUM_Inbound.ADONISReconManager;
import it.reply.NGUM_Inbound.ADONISTaskSupport;
import org.junit.Test;
import Exception.*;

import java.util.HashMap;

public class TestSuite {

    @Test
    public void testAccount() throws ADONISTargetReconManagerException {
        ADONISReconManager reconManager = new ADONISReconManager(
                "20190507_accounts_ADONIS.csv",
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
        attrs.put("File Path","20190507_accounts_ADONIS.csv");
        attrs.put("AppInstanceName","ADONIS_TEST");
        attrs.put("Lookup","Lookup.ADONIS.Roles");
        attrs.put("Batch Size","100");

        new ADONISTaskSupport().execute(attrs);
    }

}
