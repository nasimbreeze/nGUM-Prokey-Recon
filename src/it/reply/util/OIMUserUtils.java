package it.reply.util;

import Exception.InterfaceManagerException;
import Exception.OIMUserUtilsException;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.tcResultSet;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.rolemgmt.api.*;
import com.thortech.util.logging.Logger;
import it.reply.Static.CustomConstants;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.*;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.EntitlementInstance;
import oracle.iam.reconciliation.utils.Sys;

import java.util.*;


public class OIMUserUtils {
    public OIMUserUtils() {
        super();
    }

    private InterfaceManager intfMgr;
    private static Logger logger = Logger.getLogger(CustomConstants.CONNECTOR_TARGET_RECON_LOGGER);

    public OIMUserUtils(InterfaceManager intfMgr) throws InterfaceManagerException {
        this.intfMgr = intfMgr;
    }

    public Map<String, String> getLookup(String lookupName) throws OIMUserUtilsException {
        Map lookup = new HashMap();
        try {
            tcLookupOperationsIntf lookupIntf = this.intfMgr.getLegacyLookupOpsIntf();
            tcResultSet resultSet = lookupIntf.getLookupValues(lookupName);

            for (int i = 0; i < resultSet.getTotalRowCount(); i++) {
                resultSet.goToRow(i);
                lookup.put(resultSet.getStringValue("Lookup Definition.Lookup Code Information.Code Key"),
                           resultSet.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
            }
        } catch (tcAPIException e) {
            throw new OIMUserUtilsException(e);
        } catch (tcInvalidLookupException e) {
            throw new OIMUserUtilsException("Can't find specified lookup", e);
        } catch (tcColumnNotFoundException e) {
            throw new OIMUserUtilsException(e);
        } catch (InterfaceManagerException e) {
            throw new OIMUserUtilsException(e);
        }

        return lookup;
    }

    public String getItResourceKey(String applicationInstanceName) throws InterfaceManagerException {
        String itResourceKey = null;
        if (applicationInstanceName.length() == 0)
            return itResourceKey;
        try {
            ApplicationInstanceService ais = this.intfMgr.getApplicationInstanceService();
            ApplicationInstance appInst = (ApplicationInstance) ais.findApplicationInstanceByName(applicationInstanceName);

            itResourceKey = String.valueOf(appInst.getItResourceKey());
        } catch (InterfaceManagerException e) {
            e.printStackTrace();
        } catch (ApplicationInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (GenericAppInstanceServiceException e) {
            e.printStackTrace();
        }

        return itResourceKey;
    }

    public String getItResourceName(String applicationInstanceName) throws InterfaceManagerException {
        String itResourceName = null;
        if(applicationInstanceName.length() == 0)
            return itResourceName;
        try {
            ApplicationInstanceService ais = this.intfMgr.getApplicationInstanceService();
            ApplicationInstance appInst = (ApplicationInstance) ais.findApplicationInstanceByName(applicationInstanceName);

            itResourceName = String.valueOf(appInst.getItResourceName());
        } catch (InterfaceManagerException e) {
            e.printStackTrace();
        } catch (ApplicationInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (GenericAppInstanceServiceException e) {
            e.printStackTrace();
        }

        return itResourceName;
    }

    public String getItResourceObject(String applicationInstanceName) throws InterfaceManagerException {
        String objectName = null;
        if(applicationInstanceName.length() == 0)
            return objectName;
        try {
            ApplicationInstanceService ais = this.intfMgr.getApplicationInstanceService();
            ApplicationInstance appInst = (ApplicationInstance) ais.findApplicationInstanceByName(applicationInstanceName);

            objectName = String.valueOf(appInst.getObjectName());
        } catch (InterfaceManagerException e) {
            e.printStackTrace();
        } catch (ApplicationInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (GenericAppInstanceServiceException e) {
            e.printStackTrace();
        }

        return objectName;
    }

    public String getUserKey(String user_login) {
        try {
            UserManager userManager = this.intfMgr.getUserMgmtIntf();
            Set proj  = new HashSet();
            proj.add(UserManagerConstants.AttributeName.USER_KEY.getName());
            proj.add(UserManagerConstants.AttributeName.USER_LOGIN.getName());
            SearchCriteria userLogin = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getName(),user_login, SearchCriteria.Operator.EQUAL);

            List<User> users = userManager.search(userLogin, proj, null);

            if(users.size() == 0)
                return null;

            User u = users.get(0);
            return u.getEntityId();
        } catch (InterfaceManagerException e) {
            return null;
        } catch (UserSearchException e) {
            return null;
        }
    }

    public ProvisioningService getProvisioningService() {
        ProvisioningService provisioningService = this.intfMgr.getProvisioningService();
        return provisioningService;
    }

    public EntitlementService getEntitlementService() {
        EntitlementService enti = this.intfMgr.getEntitlementService();
        return enti;
    }
}
