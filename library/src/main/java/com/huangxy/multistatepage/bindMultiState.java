package com.huangxy.multistatepage;

public class bindMultiState {

    public static final int LOADING = 0;

    public static final int SUCCESS = 1;

    public static final int EMPTY = 2;

    public static final int ERROR = 3;

    public interface Convertor {
        int convert(int code);
    }
}
