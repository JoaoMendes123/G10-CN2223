import com.google.cloud.compute.v1.Instance;
import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CfunctionVmsIP implements HttpFunction {

    private static final String PROJECT_ID = "cn2223-t1-g10";
    private static final String GROUP_ZONE = "europe-north1-b";
    private static final String GROUP_NAME = "instance-group-apprest";
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        ArrayList<String> vms = new ArrayList<>();
        try (InstancesClient client = InstancesClient.create()) {
            for (Instance curInst : client.list(PROJECT_ID, GROUP_ZONE).iterateAll()) {
                if (curInst.getName().contains(GROUP_NAME)) {
                    if(curInst.getStatus().equals("RUNNING")){
                        String ip = curInst.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP();
                        vms.add(ip);
                    }
                }
            }
        }
        var parser = new Gson();
        var json = parser.toJson(vms.toArray(String[]::new));
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write(json);
        httpResponse.getWriter().flush();
    }
}
