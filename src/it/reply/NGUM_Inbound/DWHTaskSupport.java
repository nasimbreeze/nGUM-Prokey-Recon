package it.reply.NGUM_Inbound;

import oracle.iam.scheduler.vo.TaskSupport;

import java.util.HashMap;

public class DWHTaskSupport extends TaskSupport {

    public DWHTaskSupport() {
        super();
    }

    @Override
    public void execute(HashMap input) throws Exception {
        String sourceFilePath = (String) input.get("File Path");
        String lookup = (String) input.get("Lookup");
        String appInstanceName = (String) input.get("AppInstanceName");
        int batchSize = Integer.parseInt((String) input.get("Batch Size"));
        //boolean withChild = Boolean.parseBoolean((String) input.get("For Child Table"));

        // To deploy
        boolean isLocal = false;
        //boolean isLocal = true;

        new DWHReconManager(
                sourceFilePath, isLocal, appInstanceName, lookup, batchSize//, withChild
        ).runTargetRecon();
    }

    @Override
    public HashMap getAttributes() {
        return null;
    }

    @Override
    public void setAttributes() {

    }

}
