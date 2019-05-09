import it.reply.NGUM_Inbound.FKPReconManager;
import it.reply.NGUM_Inbound.FKPTaskSupport;
import org.junit.Test;
import Exception.*;

import java.util.HashMap;

public class TestSuite {

    @Test
    public void testAccount_Test() throws FKPTargetReconManagerException {
        FKPReconManager reconManager = new FKPReconManager(
                "20190507_accounts_FKP.csv",
                true,
                "FKPBestellcenterAppInst",
                "Lookup.TTWOS.EMAIL.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

    @Test
    public void testAccount_Test2() throws FKPTargetReconManagerException {
        FKPReconManager reconManager = new FKPReconManager(
                "20190507_accounts_FKP.csv",
                true,
                "FKPRechnungsanalyseAppInst",
                "Lookup.TTWOS.EMAIL.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

    @Test
    public void testTaskSupportAccount() throws Exception {
        HashMap<String,Object> attrs = new HashMap<>();
        attrs.put("File Path","20190507_accounts_FKP.csv");
        attrs.put("AppInstanceName","FKPBestellcenterAppInst");
        attrs.put("Lookup","Lookup.TTWOS.EMAIL.Roles");
        attrs.put("Batch Size","100");

        new FKPTaskSupport().execute(attrs);
    }

}
