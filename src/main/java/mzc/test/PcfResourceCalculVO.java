package mzc.test;

import java.util.HashMap;
import java.util.List;

public class PcfResourceCalculVO {
    
    List<HashMap<String, Object>> pcfEnvResourceList;
    int allApplicationSumNum;
    int allInstanceSumNum;
    public List<HashMap<String, Object>> getPcfEnvResourceList() {
        return pcfEnvResourceList;
    }
    public void setPcfEnvResourceList(List<HashMap<String, Object>> pcfEnvResourceList) {
        this.pcfEnvResourceList = pcfEnvResourceList;
    }
    public int getAllApplicationSumNum() {
        return allApplicationSumNum;
    }
    public void setAllApplicationSumNum(int allApplicationSumNum) {
        this.allApplicationSumNum = allApplicationSumNum;
    }
    public int getAllInstanceSumNum() {
        return allInstanceSumNum;
    }
    public void setAllInstanceSumNum(int allInstanceSumNum) {
        this.allInstanceSumNum = allInstanceSumNum;
    }
}
