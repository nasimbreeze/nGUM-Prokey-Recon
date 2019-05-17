import it.reply.NGUM_Inbound.TTWOSReconManager;
import Exception.*;
import org.junit.Test;

public class TestSuite {

    @Test
    public void testAccount_Test() throws FKPTargetReconManagerException {
        TTWOSReconManager reconManager = new TTWOSReconManager(
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
        TTWOSReconManager reconManager = new TTWOSReconManager(
                "20190507_accounts_FKP.csv",
                true,
                "FKPRechnungsanalyseAppInst",
                "Lookup.TTWOS.EMAIL.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

    @Test
    public void testAccount_Test3() throws FKPTargetReconManagerException {
        TTWOSReconManager reconManager = new TTWOSReconManager(
                "20190507_accounts_LDAPUID.csv",
                true,
                //"TTWOSapp3ldapuid",
                "TTWOSApp2ldapuid",
                "Lookup.TTWOS.LDAPUID.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

    @Test
    public void testAccount_Test4() throws FKPTargetReconManagerException {
        TTWOSReconManager reconManager = new TTWOSReconManager(
                "20190507_accounts_ADSAMACCOUNTNAME.csv",
                true,
                //"TTWOSapp2adsamaccount",
                "TTWOSApp3samaccountname",
                "Lookup.TTWOS.ADSAMACCOUNTNAME.Roles",
                100
        );
        reconManager.runTargetRecon();
    }

/*
    @Test
    public void testTaskSupportAccount() throws Exception {
        HashMap<String,Object> attrs = new HashMap<>();
        attrs.put("File Path","20190507_accounts_FKP.csv");
        attrs.put("AppInstanceName","FKPBestellcenterAppInst");
        attrs.put("Lookup","Lookup.TTWOS.EMAIL.Roles");
        attrs.put("Batch Size","100");

        new TTWOSTaskSupport().execute(attrs);
    }
*/
}
