package android.eq366pt.zxtnetwork.com.yg;

/**
 * Created by lbk on 2018/2/6.
 */

public class LEDOPTION {
    private String o;
    private int row;
    private int column;
    private String a;
    private int v;
    private String d;
    private int time;
    private String f;

    public LEDOPTION(String o, int row, int column, String a, int v, String d, int time, String f) {
        this.o = o;
        this.row = row;
        this.column = column;
        this.a = a;
        this.v = v;
        this.d = d;
        this.time = time;
        this.f = f;
    }

    public LEDOPTION(String o, int row, int column, String a, int v) {
        this.o = o;
        this.row = row;
        this.column = column;
        this.a = a;
        this.v = v;
    }

    public LEDOPTION(String d, int time) {
        this.d = d;
        this.time = time;
    }

    public LEDOPTION(String f,int row, int column) {
        this.row = row;
        this.column = column;
        this.f = f;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }
}
