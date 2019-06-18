import it.reply.NGUM_Inbound.TTWOSReconManager;
import Exception.*;
import org.junit.Test;

public class TestSuite {

    @Test
    public void testAccount_Test() throws FKPTargetReconManagerException {
        TTWOSReconManager reconManager = new TTWOSReconManager(
                "20190507_accounts_FKP.csv",
                true,
                "",
                "Lookup.LDAP.TRUSTED.ReconAttrMap",
                //"Lookup.LDAP.UM.ReconAttrMap.Trusted",
                100
        );
        reconManager.runTargetRecon();
    }
}
