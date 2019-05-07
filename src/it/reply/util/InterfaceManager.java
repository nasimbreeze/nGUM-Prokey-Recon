package it.reply.util;


import Exception.InterfaceManagerException;
import Thor.API.Operations.*;
import oracle.iam.identity.rolemgmt.api.*;
import com.thortech.util.logging.Logger;
import it.reply.Static.CustomConstants;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.Platform;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.reconciliation.api.ReconOperationsService;

import javax.security.auth.login.LoginException;
import java.util.Hashtable;

public class InterfaceManager {
    private OIMClient oimClient;
    private boolean isLocal;
    private static InterfaceManager intfMgr;
    private static Logger logger = Logger.getLogger(CustomConstants.CONNECTOR_TARGET_RECON_LOGGER);

    private InterfaceManager(boolean local) throws InterfaceManagerException, LoginException {
        logger.info(getClass().getName() + ".new():START:args={" + "}");
        this.isLocal=local;

        if (local) {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            try {
                String providerURL = ProvisioningUtils.getConfigProperty("PROVIDER_URL");
                logger.info(getClass().getName() + ".new():providerURL=" + providerURL);

                env.put("java.naming.provider.url", providerURL);
                String username = ProvisioningUtils.getConfigProperty("OIM_USERNAME");
                logger.info(getClass().getName() + ".new():username=" + username);

                String password = ProvisioningUtils.getConfigProperty("OIM_PASSWORD");
                logger.debug(getClass().getName() + ".new():password=" + password);
                System.setProperty("java.security.auth.login.config",
                                   System.getProperty("user.dir") + "/properties/authwl.conf");

                System.setProperty("OIM.AppServerType", "wls");
                System.setProperty("APPSERVER_TYPE", "wls");
                this.oimClient = new OIMClient(env);
                this.oimClient.login(username, password.toCharArray());
            } catch (Exception e) {
                throw new InterfaceManagerException(e);
            }
        }
        logger.info(getClass().getName() + ".new():END");
    }

    public static InterfaceManager getInstance(boolean local) throws InterfaceManagerException {
        try {
            if (intfMgr == null)
                intfMgr = new InterfaceManager(local);
        } catch (LoginException e) {
            throw new InterfaceManagerException(e);
        }
        return intfMgr;
    }

    public UserManager getUserMgmtIntf() throws InterfaceManagerException {
        UserManager userMgmtIntf = null;
        try {
            if (this.isLocal)
                userMgmtIntf = (UserManager)this.oimClient.getService(UserManager.class);
            else
                userMgmtIntf = (UserManager)Platform.getService(UserManager.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return userMgmtIntf;
    }

    public OrganizationManager getOrgMgmtIntf() throws InterfaceManagerException {
        OrganizationManager orgMgmtIntf = null;
        try {
            if (this.isLocal) {
                orgMgmtIntf = (OrganizationManager)this.oimClient.getService(OrganizationManager.class);
            } else
                orgMgmtIntf = (OrganizationManager)Platform.getService(OrganizationManager.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return orgMgmtIntf;
    }
    
    public ReconOperationsService getReconOpsIntf() throws InterfaceManagerException {
        ReconOperationsService reconOpstIntf = null;
        try {
            if (this.isLocal) {
                reconOpstIntf = (ReconOperationsService)this.oimClient.getService(ReconOperationsService.class);
            } else
                reconOpstIntf = (ReconOperationsService)Platform.getService(ReconOperationsService.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return reconOpstIntf;
    }

    public ApplicationInstanceService getApplicationInstanceService() throws InterfaceManagerException {
        ApplicationInstanceService ais = null;
        try{
            if(this.isLocal) {
                ais = (ApplicationInstanceService) this.oimClient.getService(ApplicationInstanceService.class);
            }
            else {
                ais = (ApplicationInstanceService) Platform.getService(ApplicationInstanceService.class);
            }
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }

        return ais;
    }

    public oracle.iam.identity.rolemgmt.api.RoleManager getRoleManager() {
        RoleManager rm = null;
        try{
            if(this.isLocal) {
                rm = (RoleManager) this.oimClient.getService(RoleManager.class);
            }
            else {
                rm = (RoleManager) Platform.getService(RoleManager.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rm;
    }


    public tcUserOperationsIntf getLegacyUserOpsIntf() throws InterfaceManagerException {
        tcUserOperationsIntf userOpIntf = null;
        try {
            if (this.isLocal) {
                userOpIntf = (tcUserOperationsIntf)this.oimClient.getService(tcUserOperationsIntf.class);
            } else
                userOpIntf = (tcUserOperationsIntf)Platform.getService(tcUserOperationsIntf.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return userOpIntf;
    }

    public tcFormInstanceOperationsIntf getLegacyFormInstanceOpsIntf() throws InterfaceManagerException {
        tcFormInstanceOperationsIntf intf = null;
        try {
            if (this.isLocal) {
                intf = (tcFormInstanceOperationsIntf)this.oimClient.getService(tcFormInstanceOperationsIntf.class);
            } else
                intf = (tcFormInstanceOperationsIntf)Platform.getService(tcFormInstanceOperationsIntf.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return intf;
    }

    public tcFormDefinitionOperationsIntf getLegacyFormDefinitionOpsIntf() throws InterfaceManagerException {
        tcFormDefinitionOperationsIntf intf = null;
        try {
            if (this.isLocal) {
                intf = (tcFormDefinitionOperationsIntf)this.oimClient.getService(tcFormDefinitionOperationsIntf.class);
            } else
                intf = (tcFormDefinitionOperationsIntf)Platform.getService(tcFormDefinitionOperationsIntf.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return intf;
    }

    public tcLookupOperationsIntf getLegacyLookupOpsIntf() throws InterfaceManagerException {
        tcLookupOperationsIntf intf = null;
        try {
            if (this.isLocal) {
                intf = (tcLookupOperationsIntf)this.oimClient.getService(tcLookupOperationsIntf.class);
            } else
                intf = (tcLookupOperationsIntf)Platform.getService(tcLookupOperationsIntf.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return intf;
    }
    
    public tcITResourceInstanceOperationsIntf getLegacyITResOpsIntf() throws InterfaceManagerException {
        tcITResourceInstanceOperationsIntf intf = null;
        try {
            if (this.isLocal) {
                intf = (tcITResourceInstanceOperationsIntf)this.oimClient.getService(tcITResourceInstanceOperationsIntf.class);
            } else
                intf = (tcITResourceInstanceOperationsIntf)Platform.getService(tcITResourceInstanceOperationsIntf.class);
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
        return intf;
    }

    public void logout() throws InterfaceManagerException {
        try {
            this.oimClient.logout();
        } catch (Exception e) {
            throw new InterfaceManagerException(e);
        }
    }

    public static void main(String[] args) {
        try {
            InterfaceManager i = getInstance(true);
            UserManager um = i.getUserMgmtIntf();
            //asd = i.getLegacyUserOpsIntf();
        } catch (InterfaceManagerException e) {
            tcUserOperationsIntf asd;
            e.printStackTrace();
        }
    }


    public ProvisioningService getProvisioningService() {
        ProvisioningService ps = null;
        try{
            if(this.isLocal) {
                ps = (ProvisioningService) this.oimClient.getService(ProvisioningService.class);
            }
            else {
                ps = (ProvisioningService) Platform.getService(ProvisioningService.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ps;
    }

    public EntitlementService getEntitlementService() {
        EntitlementService es = null;
        try{
            if(this.isLocal) {
                es = (EntitlementService) this.oimClient.getService(EntitlementService.class);
            }
            else {
                es = (EntitlementService) Platform.getService(EntitlementService.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return es;
    }
}


