package com.pda.birdex.pda.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class TakingOrderNoInfoEntity extends BaseEntity {
    BaseInfo1 detail = new BaseInfo1();//":

    public BaseInfo1 getDetail() {
        return detail;
    }

    public class BaseInfo1 {
        BaseInfo baseInfo = new BaseInfo();
        List<ContainerList> containerList = new ArrayList<>();
        List<OperationLog> operationLog = new ArrayList<>();

        public BaseInfo getBaseInfo() {
            return baseInfo;
        }

        public List<ContainerList> getContainerList() {
            return containerList;
        }

        public List<OperationLog> getOperationLog() {
            return operationLog;
        }

        public void setOperationLog(List<OperationLog> operationLog) {
            this.operationLog = operationLog;
        }

        public class BaseInfo {
            String takingOrderNo = "";//": "string",
            String takingType = "";//": "string",
            String isExist = "";//": true,
            String takingStatus = "";//": "string",
            String staff = "";//": "string",
            String combine = "";//": true,
            String comment = "";//": "string",
            String createTime = "";//": "2016-06-27T03:59:46.160Z",
            String takingTime = "";//": "2016-06-27T03:59:46.160Z",
            String diffTime = "";//": "string"
            Person person = new Person();

            public class Person {
                String org = "";//": "string",
                String co = "";//": "string",
                String name = "";//": "string"

                public String getOrg() {
                    return org;
                }

                public String getCo() {
                    return co;
                }

                public String getName() {
                    return name;
                }
            }

            public Person getPerson() {
                return person;
            }

            public String getTakingOrderNo() {
                return takingOrderNo;
            }

            public String getTakingType() {
                return takingType;
            }

            public String getIsExist() {
                return isExist;
            }

            public String getTakingStatus() {
                return takingStatus;
            }

            public String getStaff() {
                return staff;
            }

            public String getCombine() {
                return combine;
            }

            public String getComment() {
                return comment;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getTakingTime() {
                return takingTime;
            }

            public String getDiffTime() {
                return diffTime;
            }
        }

        public class ContainerList {
            String parcelId = "";//": "string",
            String status = "";//": "string",
            String area = "";//": "string",
            List<String> photoUrl = new ArrayList<>();//": [

            public String getParcelId() {
                return parcelId;
            }

            public String getStatus() {
                return status;
            }

            public String getArea() {
                return area;
            }

            public List<String> getPhotoUrl() {
                return photoUrl;
            }
        }

        public class OperationLog {
            String opterateTime = "";//": "2016-06-27T03:59:46.161Z",
            String operation = "";//": "string",
            String operator = "";//": "string",
            String comment = "";//": "string"

            public String getOpterateTime() {
                return opterateTime;
            }

            public String getOperation() {
                return operation;
            }

            public String getOperator() {
                return operator;
            }

            public String getComment() {
                return comment;
            }
        }
    }
}