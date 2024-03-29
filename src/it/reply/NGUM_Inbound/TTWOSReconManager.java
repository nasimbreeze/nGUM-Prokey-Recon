package it.reply.NGUM_Inbound;

import Exception.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.thortech.util.logging.Logger;
import it.reply.Static.CustomConstants;
import it.reply.util.InterfaceManager;
import it.reply.util.OIMUserUtils;
import it.reply.util.ProvisioningUtils;
import oracle.iam.reconciliation.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Thread.sleep;


public class TTWOSReconManager {

    //private TPS2ClientDataFacade dataFacade;
    private boolean isLocal;
    private boolean withChild;
    private String sourceFilePath;
    private String reconAttrMapLookupName;
    private String itResourceKey;
    private String itResourceName;
    private String itResourceObject;
    private int batchSize;
    private String[] header;
    private Map<String, String> reconAttrMap;
    private List<String> alreadyVisitedAccountPerRoles;


    private static Logger logger = Logger.getLogger(CustomConstants.CONNECTOR_TARGET_RECON_LOGGER);


    public TTWOSReconManager() {
        super();
    }

    public void setWithChild(boolean withChild) {
        this.withChild = withChild;
    }

    public TTWOSReconManager(String sourceFilePath, boolean isLocal, String appInstanceName, String reconAttrMapLookupName,
                             int batchSize/*, boolean withChild*/) throws FKPTargetReconManagerException {

        String methodName = "TTWOSReconManager()::";
        logger.debug(methodName + sourceFilePath + ";" + isLocal + ";" + reconAttrMapLookupName + ";" + batchSize);
        this.isLocal = isLocal;
		this.sourceFilePath = sourceFilePath;
		this.alreadyVisitedAccountPerRoles = new ArrayList<>();
		
		try {
			this.reconAttrMapLookupName = ProvisioningUtils.getComponentConfigProperty("", "") == null
					? reconAttrMapLookupName
					: ProvisioningUtils.getComponentConfigProperty("TPS2_Connector", "default_TPS2_ReconLookup");
		} catch (ProvisioningUtilsException e1) {
			// TODO Auto-generated catch block
			this.reconAttrMapLookupName = reconAttrMapLookupName;
		}
        this.batchSize = batchSize;
        OIMUserUtils ou;
        try {
            logger.debug(methodName + "retrieving OIMUserUtils...");
            ou = new OIMUserUtils(InterfaceManager.getInstance(isLocal));

            this.itResourceKey = ou.getItResourceKey(appInstanceName);
            this.itResourceName = ou.getItResourceName(appInstanceName);
            this.itResourceObject = ou.getItResourceObject(appInstanceName);

            logger.debug(methodName + "retrieving reconAttrMap from lookup ==> " + this.reconAttrMapLookupName);
            reconAttrMap = ou.getLookup(this.reconAttrMapLookupName);

            logger.debug(methodName + "retrieving CSV header...");

            //CSVReader reader = new CSVReader(new FileReader(sourceFilePath), ';', '"');
            //CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(sourceFilePath), "Windows-1252"), ';');

            CSVReader reader = getCSVreader(sourceFilePath);

            this.header = reader.readNext(); //mi salvo l'header
            for (int index = 0; index < this.header.length; index++)
                logger.debug(methodName + "HEADER[" + index + "]=" + this.header[index]);
            reader.close();

        } catch (InterfaceManagerException e) {
            throw new FKPTargetReconManagerException(e);
        } catch (FileNotFoundException e) {
            throw new FKPTargetReconManagerException(e);
        } catch (IOException e) {
            throw new FKPTargetReconManagerException(e);
        } catch (OIMUserUtilsException e) {
            throw new FKPTargetReconManagerException(e);
        }
    }

    private CSVReader getCSVreader(String file){
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

        BufferedReader br = null;
        try {
            br = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();

        return reader;
    }

    private EventAttributes getEventAttributes() {
        EventAttributes evtAttrs= new EventAttributes();
        evtAttrs.setEventFinished(true);
        evtAttrs.setActionDate(new Date());             // Use current date
        evtAttrs.setActionDate(null);                   // Processing will be done instantly; no deferring date
        evtAttrs.setChangeType(ChangeType.CHANGELOG);     // For create and modify operations
        return evtAttrs;
    }

    public void runTargetRecon() throws FKPTargetReconManagerException {
        if(itResourceKey == null) {
            itResourceKey = "10";
            itResourceObject = "LDAP Trusted User";
            itResourceName = "LDAP TRUSTED";
        }
        /*
         * 1-mi leggo il file con OpenCSV
         * 2-per ogni record:
         * 2.1- decodifico la GUID
         * 2.2- creo un evento di riconciliazione
         */
        String methodName = "runTargetRecon()::";

        ReconOperationsService reconOp;
        try {
            reconOp = InterfaceManager.getInstance(isLocal).getReconOpsIntf();
        } catch (InterfaceManagerException e) {
            throw new FKPTargetReconManagerException();
        }

        Map<String, Object> roDataMap;
        List<Map<String, String>> dumpedData;
        int batchIndex = 1;
        /*
         * taglio il file in blocchi, comincio dal primo batch
         */

        dumpedData = parseFile(batchIndex, batchSize);
        if (dumpedData != null)
            logger.debug(methodName + "dumpedData size=" + dumpedData.size());
        else
            logger.debug(methodName + "dumpedData NULL!");


        while (dumpedData != null && dumpedData.size() > 0) {
            /*
             * whatever
             */
            logger.debug(methodName + "whileLoop");
            if (dumpedData != null)
                logger.debug(methodName + "dumpedData size=" + dumpedData.size());
            else
                logger.debug(methodName + "dumpedData NULL!");

            ArrayList<InputData> inputDataList = new ArrayList<InputData>();
            //EventAttributes ea = new EventAttributes();
            EventAttributes ea = getEventAttributes();


            for (Map<String, String> dumpedRow : dumpedData) {
                try {
                    logMap("dumpedRow", dumpedRow);
                    //roDataMap = getMappedEntry(dumpedRow);

                    /* MAP CSV VALUES TO CONNECTOR VALUES */
                    //dumpedRow.put("itResource", this.itResourceKey);
                    //dumpedRow.put("PROFILE", dumpedRow.get("ACCESS_RIGHT_NAME"));

                    roDataMap = getMappedEntryCodeKey(dumpedRow);

                    logger.info("\n\t "+ roDataMap + "\n");

                    logMap("roDataMap", roDataMap);
                    String objectStatus = CustomConstants.STATUS_ENABLED; //evaluateObjectStatus(roDataMap);
                    logger.debug(methodName + "objectStatus=" + objectStatus);

                    boolean toOverwrite = true;
                    /*if(alreadyVisitedAccountPerRoles.contains(dumpedRow.get("Account Name")+"->"+dumpedRow.get("Last Name")))
                        toOverwrite = false;
                    else
                        alreadyVisitedAccountPerRoles.add(dumpedRow.get("Account Name") + "->" + dumpedRow.get("User ID"));*/


                    HashMap<String, List<Object>> childData = new HashMap<>();
                    List<Object> childTableList = new ArrayList<>();
                    HashMap<String, Object> childTableValues = new HashMap<>();

                    /*String[] roles = dumpedRow.get("PROFILE").split(",");
                    for(String r : roles) {
                        childTableValues.put("Role", itResourceKey + "~" + r.trim() );
                        childTableList.add(childTableValues);
                        childTableValues = new HashMap<>();
                    }
                    childData.put("Roles",childTableList);*/

                    /*if(dumpedRow.get("PROFILE") != null && dumpedRow.get("PROFILE").trim().length() > 0)
                        inputDataList.add(new InputData((HashMap) roDataMap, (HashMap) childData, toOverwrite, ea.getChangeType(), new Date()));
                    else*/
                        inputDataList.add(new InputData((HashMap) roDataMap, null, toOverwrite, ea.getChangeType(), new Date()));

                } catch (Exception e) {
                    logger.error(methodName + "ERROR", e);
                    e.printStackTrace();
                }

            }

            InputData[] ids;
            ids = new InputData[inputDataList.size()];
            int count = 0;
            for (InputData i : inputDataList) {
                ids[count] = i;
                count++;
            }
            logger.info(methodName + "creating reconciliation events batch...");

            BatchAttributes ba = new BatchAttributes(itResourceObject, null, true);

            ReconciliationResult rr = reconOp.createReconciliationEvents(ba, ids);
            ArrayList<FailedInputData> fails = rr.getFailedResult();

            logger.error("FAILS:\n");
            for(FailedInputData fid : fails) {
                logger.error(fid.getInputData().toString() + "\t" + fid.getFailure().toString());
            }

            logger.info(methodName + "...done!");
            //passo al batch successivo
            if (dumpedData.size() == batchSize) {
                batchIndex += batchSize;
                dumpedData = parseFile(batchIndex, batchSize);
                logger.info(methodName + "batchIndex=" + batchIndex);
                logger.info(methodName + "more rows to read");

            } else {
                batchIndex += dumpedData.size();
                dumpedData = null;
                logger.info(methodName + "reached EOF");
            }

        }

    }

    private String removeAccountSuffix(String username) {
        String[] splitted = username.split("_");
        if(splitted.length == 1)
            return username;

        String ldapuid = "";
        for(int i = 0; i < splitted.length - 1; i++)
            ldapuid += splitted[i];
        return ldapuid;
    }

    private Map<String, Object> getMappedEntry(Map<String, String> record) throws FKPTargetReconManagerException {
        Map<String, Object> mappedEntry = new HashMap<String, Object>();

        for (Map.Entry<String, String> recordAttribute : record.entrySet()) {
            String originalKey = recordAttribute.getKey();
            String originalValue = recordAttribute.getValue();
            String mappedKey = reconAttrMap.get(originalKey);
            if (mappedKey != null)
                mappedEntry.put(mappedKey, originalValue);
        }

        return mappedEntry;
    }

    private Map<String, Object> getMappedEntryCodeKey(Map<String, String> record) throws FKPTargetReconManagerException {
        Map<String, Object> mappedEntry = new HashMap<String, Object>();

        for (Map.Entry<String, String> recordAttribute : record.entrySet()) {
            String originalKey = recordAttribute.getKey();
            String originalValue = recordAttribute.getValue();
            String mappedKey = originalKey; // reconAttrMap.get(originalKey); //originalKey
            if (mappedKey != null)
                mappedEntry.put(mappedKey, originalValue);
        }

        return mappedEntry;
    }

    /*private List<TpsTargetReconDump> loadDumpData() {
        List<TpsTargetReconDump> result = new ArrayList<TpsTargetReconDump>();
        List<TpsTargetReconDump> dumpData = dataFacade.findAllTpsTargetReconDump();
        result.addAll(dumpData);
        return result;

    }*/


    private List<Map<String, String>> parseFile(int line, int batchSize) throws FKPTargetReconManagerException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        CSVReader reader;
        try {
            //reader = new CSVReader(new FileReader(sourceFilePath), ';', '"', line);
            reader = new CSVReader(new InputStreamReader(new FileInputStream(sourceFilePath), "Windows-1252"), ';', '"', line);

            //reader = getCSVreader(sourceFilePath);

            //String[] header = reader.readNext(); //mi salvo l'header
            String[] nextLine;
            //TpsTargetReconDump dumpRow;
            int currentLine = line;
            while ((nextLine = reader.readNext()) != null && currentLine < line + batchSize) {
                Map<String, String> parsedRow = new HashMap<String, String>();
                for (int i = 0; i < header.length; i++) {
                    parsedRow.put(header[i], nextLine[i]);
                }
                logger.info("Source FilePath:"+sourceFilePath+" Line:"+line+ " currLine:"+currentLine+ " batchSize:"+batchSize);
                result.add(parsedRow);
                currentLine++;
            }
        } catch (FileNotFoundException e) {
            throw new FKPTargetReconManagerException(e);
        } catch (IOException e) {
            throw new FKPTargetReconManagerException(e);
        }
        return result;
    }

    private void logMap(String mapName, Map inputMap) {
        logger.debug("printing map content:" + mapName);
        if (inputMap == null)
            logger.debug("map is NULL!:" + mapName);
        else {
            for (Object o : inputMap.keySet()) {
                try {
                    logger.debug(o.toString() + "=>" + inputMap.get(o).toString());
                } catch (NullPointerException e) {

                }
            }
        }
    }

}

