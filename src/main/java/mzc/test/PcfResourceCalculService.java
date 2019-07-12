package mzc.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cloudfoundry.client.v2.applications.ApplicationResource;
import org.cloudfoundry.client.v2.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.OrganizationResource;
import org.cloudfoundry.client.v2.organizations.Organizations;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class PcfResourceCalculService {
    
    public PcfResourceCalculVO getPcfAuthInfo() {
        List<HashMap<String, Object>> resultVoList = new ArrayList<HashMap<String, Object>>();
        PcfResourceCalculVO vo = new PcfResourceCalculVO();
        JSONObject pcfAdminJson = getPcfAuthInfoJsonInfo();
        if(pcfAdminJson != null) {
            try {
                JSONArray jsonArray = pcfAdminJson.getJSONArray("pcf_admin_info");
                if(jsonArray != null && jsonArray.length() != 0) {
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String api = jsonObject.getString("api");
                        String username = jsonObject.getString("username");
                        String password = jsonObject.getString("password");
                        String env = jsonObject.getString("env");
                        PcfBuildUtils utils = new PcfBuildUtils();
                        
                        DefaultConnectionContext getApiConnect = utils.connectionContext(api);
                        PasswordGrantTokenProvider getToken = utils.tokenProvider(username, password);
                        ReactorCloudFoundryClient client = utils.cloudFoundryClient(getApiConnect, getToken);
                        resultVoList.add(getPcfResourceInfo(client, env));
                    }
                }
                int allEnvTotalAppNum = 0;
                int allEnvTotalInstanceNum = 0;
                for(int i=0; i<resultVoList.size(); i++) {
                    System.out.println("------------------------------ENV RESULT START_"+resultVoList.get(i).get("env")+"----------------------------------------");
                    System.out.println("env : " + resultVoList.get(i).get("env"));
                    System.out.println("env_result : " + resultVoList.get(i).get("envResult"));
                    System.out.println("env_totla_app_num : " + resultVoList.get(i).get("envTotalAppNum"));
                    System.out.println("env_total_instance_num : " + resultVoList.get(i).get("envTotalInstanceNum"));
                    System.out.println("------------------------------ENV RESULT END_"+resultVoList.get(i).get("env")+"-----------------------------------------");
                    
                    allEnvTotalAppNum = allEnvTotalAppNum + (int) resultVoList.get(i).get("envTotalAppNum");
                    allEnvTotalInstanceNum = allEnvTotalInstanceNum + (int) resultVoList.get(i).get("envTotalInstanceNum");
                }
                System.out.println("------------------------------ALL ENV RESULT START----------------------------------------");
                System.out.println("all_env_total_app_num : " + allEnvTotalAppNum);
                System.out.println("all_env_total_instance_num : " + allEnvTotalInstanceNum);
                System.out.println("------------------------------ALL ENV RESULT END-----------------------------------------");
                
                vo.setPcfEnvResourceList(resultVoList);
                vo.setAllApplicationSumNum(allEnvTotalAppNum);
                vo.setAllInstanceSumNum(allEnvTotalInstanceNum);
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return vo;
    }

    private HashMap<String, Object> getPcfResourceInfo(ReactorCloudFoundryClient client, String env) {
        HashMap<String, Object> pcfResource = setOrgs(client, env);
        return pcfResource;
    }

    private HashMap<String, Object> setOrgs(ReactorCloudFoundryClient client, String env) {
        Organizations orgClinet = client.organizations();
        
        List<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>();
        
        List<OrganizationResource> orgList = orgClinet.list(ListOrganizationsRequest.builder().name("").build()).block().getResources();
        if(orgList != null && orgList.size() != 0) {
            String orgName = "";
            String orgId = "";
            for(int i=0; i<orgList.size(); i++) {
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                orgName = orgList.get(i).getEntity().getName();
                orgId = orgList.get(i).getMetadata().getId();
                List<HashMap<String, Object>> spaceList = setSpaces(client, orgId, resultMap);
                
                resultMap.put("org", orgName);
                resultMap.put("space", spaceList);
                resultMapList.add(resultMap);
            }
        }
        int envTotalAppNum = 0;
        int envTotalInstanceNum = 0;
        int envTotalSpaceNum = 0;
        for(int i=0; i<resultMapList.size(); i++) {
            List<HashMap<String, Object>> spacesMap = (List<HashMap<String, Object>>) resultMapList.get(i).get("space");
            for(int j=0; j<spacesMap.size(); j++) {
                if(spacesMap.get(j).get("appNums") != null && spacesMap.get(j).get("instanceNums") != null) {
                    envTotalAppNum = envTotalAppNum + (int) spacesMap.get(j).get("appNums");
                    envTotalInstanceNum = envTotalInstanceNum + (int) spacesMap.get(j).get("instanceNums");
                    envTotalSpaceNum = envTotalSpaceNum + 1;
                }
            }
        }
        
        HashMap<String, Object> pcfTotalResource = new HashMap<String, Object>();
        pcfTotalResource.put("env", env);
        pcfTotalResource.put("envResult", resultMapList);
        pcfTotalResource.put("envTotalAppNum", envTotalAppNum);
        pcfTotalResource.put("envTotalInstanceNum", envTotalInstanceNum);
        pcfTotalResource.put("envTotalSpaceNum", envTotalSpaceNum);
        return pcfTotalResource;
    }

    private List<HashMap<String, Object>> setSpaces(ReactorCloudFoundryClient client, String orgId, HashMap<String, Object> resultMap) {
        List<SpaceResource> spaceList = client.spaces().list(ListSpacesRequest.builder().organizationId(orgId).name("").build()).block().getResources();
        List<HashMap<String, Object>> spaceNameMapList = new ArrayList<HashMap<String, Object>>();
        if(spaceList != null && spaceList.size() != 0) {
            String spaceName = "";
            String spaceId = "";
            for(int i=0; i<spaceList.size(); i++) {
                
                HashMap<String, Object> spaceMap = new HashMap<String, Object>();
                spaceName = spaceList.get(i).getEntity().getName();
                spaceId = spaceList.get(i).getMetadata().getId();
                spaceMap.put("space", spaceName);
                
                setApps(client, orgId, spaceId, spaceMap);
                spaceNameMapList.add(spaceMap);
            }
        }
        return spaceNameMapList;
    }

    private void setApps(ReactorCloudFoundryClient client, String orgId, String spaceId,
            HashMap<String, Object> spaceMap) {
        
        int totalPage = client.applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgId).spaceId(spaceId).build()).block().getTotalPages();
        int totalAppNum  = client.applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgId).spaceId(spaceId).build()).block().getTotalResults();
        
        int instances = 0;
        for(int j=1; j<=totalPage; j++) {
            List<ApplicationResource> appList = client.applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgId).spaceId(spaceId).page(j).build()).block().getResources();
            if(appList != null && appList.size() != 0) {
                for(int i=0; i<appList.size(); i++) {
                    instances = instances + appList.get(i).getEntity().getInstances();
                }

            }
        }
        spaceMap.put("instanceNums", instances);
        spaceMap.put("appNums", totalAppNum);
        
    }

    private JSONObject getPcfAuthInfoJsonInfo() {
        ClassPathResource resource = new ClassPathResource("pcf_auth_info.json");
        InputStream input = null;
        BufferedReader bufferedReader= null;
        JSONObject jsonObject = null;
        try {
            input = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            try {
                jsonObject = new JSONObject(responseStrBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
