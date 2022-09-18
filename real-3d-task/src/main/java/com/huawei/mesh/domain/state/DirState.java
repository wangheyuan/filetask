package com.huawei.mesh.domain.state;

import com.huawei.mesh.domain.state.db.AbstractEnumConverter;
import com.huawei.mesh.domain.state.db.PersistEnum2DB;

/**
 * @author ：why
 * @description: TODO
 * @date ：2022/09/17 8:25
 */
public enum DirState implements PersistEnum2DB<Integer> {
    PRE_READY(0, "等待上传"),
    READ(1, "已上传"),
    Task(2, "任务处理");

    private int code;
    private String msg;

    DirState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public Integer getData() {
        return code;
    }


    public static class Converter extends AbstractEnumConverter<DirState, Integer> {

        public Converter() {
            super(DirState.class);
        }
    }
}
