package com.huawei.mesh.domain.state;

import com.huawei.mesh.domain.state.db.AbstractEnumConverter;
import com.huawei.mesh.domain.state.db.PersistEnum2DB;

public enum FileState  implements PersistEnum2DB<Integer> {
    PRE_READY(0, "等待上传或等待处理"),
    READ(1, "已上传或已上传结束");

    private int code;
    private String msg;

    FileState(int code, String msg) {
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


    public static class Converter extends AbstractEnumConverter<FileState, Integer> {

        public Converter() {
            super(FileState.class);
        }
    }
}
